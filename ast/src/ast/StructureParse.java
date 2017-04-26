/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import static global.component.GlobalComponent.codeTree;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import template.TreeRender;

/**
 *
 * @author RACHMAD
 */
public class StructureParse 
{
    private DefaultTreeModel model;
    private DefaultMutableTreeNode _package;
    private DefaultMutableTreeNode _class,_for,_while,_whileBody,_do,_doBody,_method,_if,_else,
                                   _declare,_then,_forBody,_ifBody,_elseBody,_try,_tryBody,_final,_fibalBody,
                                   _catch,_catchBody,_methodCall,_constructorCall,_field, _assignment, _array,
                                   _statement, _switch, _case, _break;
    public void setCode(JTextPane textCode)
    {
        ASTParser parser = ASTParser.newParser(AST.JLS2);
        parser.setSource(textCode.getText().toCharArray());
        //parser.setSource("/*abc*/".toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        _package = new DefaultMutableTreeNode("root");
        
        codeTree.setRootVisible(false);
        
        
        cu.accept(new ASTVisitor() 
        {
            Set names = new HashSet();
            
            /*
             * Package declaration
            */
            @Override
            public boolean visit(PackageDeclaration node)
            {
                _package = new DefaultMutableTreeNode(node.getName().toString());
                codeTree.setRootVisible(true);
                System.out.println("Package : " + node.getName().toString());
                return false;
            }
            
            /*
             * Class Declaration.
             * sub node from package
            */
            @Override
            public boolean visit(TypeDeclaration node)
            {
                String type;
                if(node.isInterface())
                    type="interface ";
                else
                    type="class ";
                _class=new DefaultMutableTreeNode(type+node.getName());
                System.out.println(type+node.getName());
                
                _package.add(_class);
                
                String superClass = node.getSuperclass()+"";
                if(!(superClass.equals("null")||(superClass.equals(""))))
                {
                    _class.add(new DefaultMutableTreeNode("extends "+superClass));
                    System.out.println("Extends : "+superClass);
                }
                
                for(Object getInterface : node.superInterfaces())
                {
                    _class.add(new DefaultMutableTreeNode("implements " + getInterface));
                    System.out.println("Implements : "+getInterface);
                }
                return true;
            }
            
            /*
             * method declaration
             * sub node from class
            */
            @Override
            public boolean visit(MethodDeclaration node)
            {
                _method = new DefaultMutableTreeNode(node.getName());
                _class.add(_method);
                System.out.println("MethodDeclaration : "+node.getName());
                return true;
            }
            
            /*
             * field declaration
             * sub node from class
            */
//            @Override
//            public boolean visit(FieldDeclaration node)
//            {
//                _field=new DefaultMutableTreeNode(node.toString());
//                _class.add(_field);
//                Object o = node.fragments().get(0);
//                if(o instanceof VariableDeclarationFragment){
//                    _method.add(new DefaultMutableTreeNode(o.toString()));
//                }
//                return false;
//            }
            
            /*
             * Variable declaration fragment AST node type
             * used in field declarations
             * local variable declarations
             * sub node from method
            */
            @Override
            public boolean visit(VariableDeclarationFragment node) 
            {
                if(node.getParent() instanceof FieldDeclaration)
                {
                    FieldDeclaration declaration = ((FieldDeclaration) node.getParent());
                    _class.add(new DefaultMutableTreeNode(declaration.getType().toString()));
                    System.out.println("FieldDeclaration : "+declaration.getType().toString());
                }
                else
                {   
                    _method.add(new DefaultMutableTreeNode(node.toString()));
                    System.out.println("VariableDeclarationFragment : "+node.toString());
                }
                return false; // do not continue to avoid usage info
            }
            
            @Override
            public boolean visit(VariableDeclarationStatement node)
            {
                _method.add(new DefaultMutableTreeNode(node.toString()));
                System.out.println("VariableDeclarationStatement : "+node.toString());
                return false;
            }
            
            /*
             * Simple Name
             * A simple name is an identifier other than a keyword, boolean literal ("true", "false") or null literal ("null"). 
            */
            @Override
            public boolean visit(SimpleName node) 
            {
                if (this.names.contains(node.getIdentifier())) {
                    _method.add(new DefaultMutableTreeNode(node.toString()));
                    System.out.println("SimpleNode Identifier : "+node.toString());
                }
                return false;
            }
            
            /*
             * Alternate constructor invocation (calling constructor) statement AST node type
             * For JLS2: 
            */
            @Override
            public boolean visit(SuperConstructorInvocation node)
            {
                _constructorCall=new DefaultMutableTreeNode(node);
                _method.add(_constructorCall);
                System.out.println("SuperConstructorInvocation : "+node);
                return false;
            }
            
            /*
             * method call
            */
            @Override
            public boolean visit(MethodInvocation node)
            {
                _methodCall=new DefaultMutableTreeNode(node);
                _method.add(_methodCall);
                System.out.println("MethodInvocation : "+node);
                return false;
            }
            
            @Override
            public boolean visit(SuperMethodInvocation node)
            {
                _methodCall=new DefaultMutableTreeNode(node);
                _method.add(_methodCall);
                System.out.println("SuperMethodInvocation : "+node);
                return false;
            }
            
            @Override
            public boolean visit(ReturnStatement node)
            {
                _method.add(new DefaultMutableTreeNode(node.toString()));
                System.out.println("ReturnStatement : "+node.toString());
                return false;
            }
            
            @Override
            public boolean visit(IfStatement node)
            {
                String elseExist="";
                _if=new DefaultMutableTreeNode("if ("+node.getExpression()+")");
                _method.add(_if);
                System.out.println("if : "+ node.getExpression());
                enclose(node.getThenStatement().toString(),_if);
                elseExist=node.getElseStatement()+"";
                if(!(elseExist.equals("")||elseExist.equals("null")))
                {
                    _else=new DefaultMutableTreeNode("else");
                    _method.add(_else);
                    System.out.println("else : "+node.getElseStatement().toString());
                    enclose(node.getElseStatement().toString(),_else);
                }
                return false;
            }
            
            @Override
            public boolean visit(EnhancedForStatement node)
            {
                _for=new DefaultMutableTreeNode("for ("+node.getParameter()+" : "+node.getExpression()+")");
                _method.add(_for);
                System.out.println("EnchancedFor : ("+node.getParameter()+" : "+node.getExpression()+")");
                enclose(node.getBody()+"",_for);
                return false;
            }
            
            @Override
            public boolean visit(ForStatement node)
            {
                /*
                 * because initial may more than 1.
                */
                String initial = "";
                for(int i=0; i<node.initializers().size(); i++)
                {
                    initial += node.initializers().get(i);
                    if(node.initializers().size()-1 != i)
                        initial += ", ";
                }
                
                /*
                 * because increment may more than 1
                */
                String inc = "";
                for(int i=0; i<node.updaters().size(); i++)
                {
                    inc += node.updaters().get(i);
                    if(node.updaters().size()-1 != i)
                        inc += ", ";
                }
                
                _for=new DefaultMutableTreeNode("for ("+initial+"; "+node.getExpression()+"; " + inc + ")");
                _method.add(_for);
                System.out.println("for ("+initial+"; "+node.getExpression()+"; " + inc + ")");
                enclose(node.getBody().toString(),_for);
                return false;
            }
            
            @Override
            public boolean visit(WhileStatement node)
            {
                _while=new DefaultMutableTreeNode("while "+node.getExpression());
                _method.add(_while);
                System.out.println("WhileStatement : "+node.getExpression());
                enclose(node.getBody().toString(),_while);
                return false;
            }
            
            @Override
            public boolean visit(DoStatement node)
            {
                _do=new DefaultMutableTreeNode("do");
                _method.add(_do);
                System.out.println("Do");
                enclose(node.getBody().toString(),_do);
                _while=new DefaultMutableTreeNode("while("+node.getExpression()+")");
                _method.add(_while);
                System.out.println("WhileDo : "+node.getExpression());
                return false;
            }
            
            @Override
            public boolean visit(TryStatement node)
            {
                String ada="";
                _try=new DefaultMutableTreeNode("try");
                _method.add(_try);
                System.out.println("try");
                enclose(node.getBody().toString(),_try);
                ada=node.getFinally()+"";
                if(!(ada.equals("")||ada.equals("null")))
                {
                    _final=new DefaultMutableTreeNode("finally");
                    _method.add(_final);
                    System.out.println("finall");
                    enclose(node.getFinally().toString(),_final);
                }
                return false;
            }
            
            @Override
            public boolean visit(CatchClause node)
            {
                _catch=new DefaultMutableTreeNode("catch ("+node.getException()+")");
                _method.add(_catch);
                System.out.println("catch : " + node.getException());
                enclose(node.getBody().toString(),_catch);
                return false;
            }
            
            @Override
            public boolean visit(Assignment node)
            {
                _assignment = new DefaultMutableTreeNode(node.toString());
                _method.add(_assignment);
                System.out.println("Assignment : "+node.toString());
                return false;
            }
            
            @Override
            public boolean visit(ConstructorInvocation node)
            {
                _constructorCall = new DefaultMutableTreeNode(node.toString());
                _method.add(_constructorCall);
                System.out.println(node.toString());
                return false;
            }
            
            @Override
            public boolean visit(AnonymousClassDeclaration node)
            {
                _constructorCall = new DefaultMutableTreeNode(node.toString());
                _method.add(_constructorCall);
                System.out.println("AnonymousClassDeclaration : "+node.toString());
                return false;
            }
            
            @Override
            public boolean visit(ArrayAccess node)
            {
                _class = new DefaultMutableTreeNode(node.toString());
                _method.add(_class);
                System.out.println("AbstrackTypeDeclaration : "+node.toString());
                return false;
            }
            
            @Override
            public boolean visit(ArrayCreation node)
            {
                _array = new DefaultMutableTreeNode(node.toString());
                _method.add(_array);
                System.out.println("ArrayCreation : " + node.toString());
                return false;
            }
            
            @Override
            public boolean visit(ArrayInitializer node)
            {
                _array = new DefaultMutableTreeNode(node.toString());
                System.out.println("ArrayInitialize : "+node.toString());
                _method.add(_array);
                return false;
            }
            
            @Override
            public boolean visit(AssertStatement node)
            {
                _statement = new DefaultMutableTreeNode(node.toString());
                System.out.println("AssertStatement : "+node.toString());
                _method.add(_statement);
                return false;
            }
            
            @Override
            public boolean visit(ContinueStatement node)
            {
                _statement = new DefaultMutableTreeNode(node.toString());
                System.out.println("ContinueStatement : "+node.toString());
                _method.add(_statement);
                return false;
            }
            
            @Override
            public boolean visit(SwitchStatement node)
            {
                _switch = new DefaultMutableTreeNode("switch ("+node.getExpression()+")");
                System.out.println("switch ("+node.getExpression()+")");
                _method.add(_switch);
                List getStatement = node.statements();
                for(Object st : getStatement)
                {
                    Matcher _caseMatch = Pattern.compile("^case\\s+.+\\:").matcher(st.toString());
                    if(_caseMatch.find())
                    {
                        _case = new DefaultMutableTreeNode(_caseMatch.group());
                        _switch.add(_case);
                    }
                    
                    enclose(st.toString(), _case);
                    
                    Matcher _breakMatch = Pattern.compile("^break\\s*.*;").matcher(st.toString());
                    if(_breakMatch.find())
                    {
                        _break = new DefaultMutableTreeNode(_breakMatch.group());
                        _case.add(_break);
                    }
                }
                return false;
            }
            
            @Override
            public boolean visit(ClassInstanceCreation node)
            {
                _constructorCall = new DefaultMutableTreeNode(node.toString());
                System.out.println("ClassInstanceCreation : "+node.toString());
                _method.add(_constructorCall);
                return false;
            }
        });
        
//        model = new DefaultTreeModel(_package);
//        model.reload();
//        codeTree.setModel(model);
        codeTree.setModel(new DefaultTreeModel(_package){
            public void reload(TreeNode node) {
                if(node != null) {
                    fireTreeStructureChanged(this, getPathToRoot(node), null, null);
                }
            }
        });
        codeTree.setCellRenderer(new TreeRender());
//        ((DefaultTreeModel)codeTree.getModel()).reload();
        for (int i = 0; i < codeTree.getRowCount(); i++)
            codeTree.expandRow(i);
    }
    
    private void enclose(String body, DefaultMutableTreeNode parentNode)
    {
        ASTParser parser = ASTParser.newParser(AST.JLS2);
        /*
         * make parse result into OPP structure, because AST wanna be that.
        */
        String setClass="class fo{\nvoid foo(){ \n"+body+"\n}\n}";
        parser.setSource(setClass.toCharArray());
        //parser.setSource("/*abc*/".toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        //ASTNode node = parser.createAST(null);
        parser.setResolveBindings(true);
        
        try
        {
            final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

            cu.accept(new ASTVisitor()
            {
                Set names2 = new HashSet();

                @Override
                public boolean visit(SimpleName node) 
                {
                    if (this.names2.contains(node.getIdentifier())) 
                    {
                        parentNode.add(new DefaultMutableTreeNode(node.toString()));
                        System.out.println("SimpleNode : "+node.toString());
                    }
                    return true;
                }

                @Override
                public boolean visit(EnhancedForStatement node)
                {
                    _for=new DefaultMutableTreeNode("for ("+node.getParameter()+" : "+node.getExpression()+")");
                    parentNode.add(_for);
                    System.out.println("for ("+node.getParameter()+" : "+node.getExpression()+")");
                    enclose(node.getBody().toString(),_for);
                    return false;
                }
                
                @Override
                public boolean visit(ForStatement node)
                {
                    /*
                     * because initial may more than 1.
                    */
                    String initial = "";
                    for(int i=0; i<node.initializers().size(); i++)
                    {
                        initial += node.initializers().get(i);
                        if(node.initializers().size()-1 != i)
                            initial += ", ";
                    }

                    /*
                     * because increment may more than 1
                    */
                    String inc = "";
                    for(int i=0; i<node.updaters().size(); i++)
                    {
                        inc += node.updaters().get(i);
                        if(node.updaters().size()-1 != i)
                            inc += ", ";
                    }

                    _for=new DefaultMutableTreeNode("for ("+initial+"; "+node.getExpression()+"; " + inc + ")");
                    parentNode.add(_for);
                    System.out.println("for ("+initial+"; "+node.getExpression()+"; " + inc + ")");
                    enclose(node.getBody().toString(),_for);
                    return false;
                }

                @Override
                public boolean visit(IfStatement node)
                {
                    String elseExist="";
                    _if=new DefaultMutableTreeNode("if ("+node.getExpression()+")");
                    System.out.println("if (+"+node.getExpression()+")");
                    parentNode.add(_if);
                    enclose(node.getThenStatement().toString(),_if);
                    elseExist=node.getElseStatement()+"";
                    if(!(elseExist.equals("")||(elseExist.equals("null"))))
                    {
                        _else=new DefaultMutableTreeNode("else");
                        System.out.println("else");
                        parentNode.add(_else);
                        enclose(node.getElseStatement().toString(),_else);
                    }
                    return false;
                }

                @Override
                public boolean visit(VariableDeclarationFragment node) 
                {
                    if(node.getParent() instanceof FieldDeclaration)
                    {
                        FieldDeclaration declaration = ((FieldDeclaration) node.getParent());
                        _class.add(new DefaultMutableTreeNode(declaration.getType().toString()));
                    }
                    else
                    {   
                        System.out.println("VariableDeclarationFragment : "+node.toString());
                        parentNode.add(new DefaultMutableTreeNode(node.toString()));
                    }
                    return false; // do not continue to avoid usage info
                }

                @Override
                public boolean visit(ReturnStatement node)
                {
                    parentNode.add(new DefaultMutableTreeNode(node.toString()));
                    System.out.println("Return : "+node.toString());
                    return false;
                }

                @Override
                public boolean visit(SuperConstructorInvocation node)
                {
                    _constructorCall=new DefaultMutableTreeNode(node);
                    parentNode.add(_constructorCall);
                    System.out.println("SuperConstructorInvocation : "+node);
                    return false;
                }

                @Override
                public boolean visit(MethodInvocation node)
                {
                    _methodCall=new DefaultMutableTreeNode(node);
                    parentNode.add(_methodCall);
                    System.out.println("MethodInvocation : "+node);
                    return true;
                }

                @Override
                public boolean visit(SuperMethodInvocation node)
                {
                    _methodCall=new DefaultMutableTreeNode(node);
                    parentNode.add(_methodCall);
                    System.out.println("SuperMethodInvocation : "+node);
                    return false;
                }

                @Override
                public boolean visit(WhileStatement node)
                {
                    _while=new DefaultMutableTreeNode("while "+node.getExpression());
                    parentNode.add(_while);
                    System.out.println("WhileStatement : "+node.getExpression());
                    enclose(node.getBody().toString(),_while);
                    return false;
                }

                @Override
                public boolean visit(DoStatement node)
                {
                    _do=new DefaultMutableTreeNode("do");
                    parentNode.add(_do);
                    System.out.println("do");
                    enclose(node.getBody().toString(),_do);
                    _while=new DefaultMutableTreeNode("while("+node.getExpression()+")");
                    parentNode.add(_while);
                    return false;
                }

                @Override
                public boolean visit(TryStatement node)
                {
                    String ada="";
                    _try=new DefaultMutableTreeNode("try");
                    parentNode.add(_try);
                    System.out.println("try");
                    enclose(node.getBody().toString(),_try);
                    ada=node.getFinally()+"";
                    if(!(ada.equals("")||(ada.equals("null"))))
                    {
                        _final=new DefaultMutableTreeNode("finally");
                        parentNode.add(_final);
                        System.out.println("finally");
                        enclose(node.getFinally().toString(),_final);
                    }
                    return false;
                }

                @Override
                public boolean visit(CatchClause node)
                {
                    _catch=new DefaultMutableTreeNode("catch ("+node.getException()+")");
                    parentNode.add(_catch);
                    System.out.println("catch : "+node.getException());
                    enclose(node.getBody().toString(),_catch);
                    return false;
                }
                
                @Override
                public boolean visit(Assignment node)
                {
                    _assignment = new DefaultMutableTreeNode(node.toString());
                    parentNode.add(_assignment);
                    System.out.println("Assignment : "+node.toString());
                    return false;
                }
                
                @Override
                public boolean visit(ConstructorInvocation node)
                {
                    _constructorCall = new DefaultMutableTreeNode(node.toString());
                    parentNode.add(_constructorCall);
                    System.out.println(node.toString());
                    return false;
                }
                
                @Override
                public boolean visit(AnonymousClassDeclaration node)
                {
                    _constructorCall = new DefaultMutableTreeNode(node.toString());
                    parentNode.add(_constructorCall);
                    System.out.println("AnonymousClassDeclaration : "+node.toString());
                    return false;
                }
                
                @Override
                public boolean visit(ArrayAccess node)
                {
                    _class = new DefaultMutableTreeNode(node.toString());
                    parentNode.add(_class);
                    System.out.println("AbstrackTypeDeclaration : "+node.toString());
                    return false;
                }
                
                @Override
                public boolean visit(ArrayCreation node)
                {
                    _array = new DefaultMutableTreeNode(node.toString());
                    _method.add(_array);
                    System.out.println("ArrayCreation : "+node.toString());
                    return false;
                }
                
                @Override
                public boolean visit(ArrayInitializer node)
                {
                    _array = new DefaultMutableTreeNode(node.toString());
                    System.out.println("ArrayInitialize : "+node.toString());
                    parentNode.add(_array);
                    return false;
                }
                
                @Override
                public boolean visit(AssertStatement node)
                {
                    _statement = new DefaultMutableTreeNode(node.toString());
                    System.out.println("AssertStatement : "+node.toString());
                    parentNode.add(_statement);
                    return false;
                }
                
                @Override
                public boolean visit(ContinueStatement node)
                {
                    _statement = new DefaultMutableTreeNode(node.toString());
                    System.out.println("ContinueStatement : "+node.toString());
                    parentNode.add(_statement);
                    return false;
                }
                
                @Override
                public boolean visit(SwitchStatement node)
                {
                    _switch = new DefaultMutableTreeNode("switch ("+node.getExpression()+")");
                    System.out.println("switch ("+node.getExpression()+")");
                    parentNode.add(_switch);
                    List getStatement = node.statements();
                    for(Object st : getStatement)
                    {
                        Matcher _caseMatch = Pattern.compile("^case\\s+.+\\:").matcher(st.toString());
                        if(_caseMatch.find())
                        {
                            _case = new DefaultMutableTreeNode(_caseMatch.group());
                            _switch.add(_case);
                        }

                        enclose(st.toString(), _case);

                        Matcher _breakMatch = Pattern.compile("^break\\s*.*;").matcher(st.toString());
                        if(_breakMatch.find())
                        {
                            _break = new DefaultMutableTreeNode(_breakMatch.group());
                            _case.add(_break);
                        }
                    }
                    return false;
                }
                
                @Override
                public boolean visit(ClassInstanceCreation node)
                {
                    _constructorCall = new DefaultMutableTreeNode(node.toString());
                    System.out.println("ClassInstanceCreation : "+node.toString());
                    parentNode.add(_constructorCall);
                    return false;
                }
            });
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public DefaultTreeModel getTreeModel()
    {
        return model;
    }
}
