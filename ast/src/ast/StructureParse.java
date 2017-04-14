/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import static global.component.GlobalComponent.codeTree;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JTextPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
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
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 *
 * @author RACHMAD
 */
public class StructureParse 
{
    private DefaultTreeModel model;
    private DefaultMutableTreeNode _package = new DefaultMutableTreeNode("root");
    private DefaultMutableTreeNode _class,_for,_while,_whileBody,_do,_doBody,_method,_if,_else,
                                   _declare,_then,_forBody,_ifBody,_elseBody,_try,_tryBody,_final,_fibalBody,
                                   _catch,_catchBody,_methodCall,_constructorCall,_field;
    public void setCode(JTextPane textCode)
    {
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(textCode.getText().toCharArray());
        //parser.setSource("/*abc*/".toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        
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
                _package = new DefaultMutableTreeNode(node.getName()+"");
                codeTree.setRootVisible(true);
                return false;
            }
            
            /*
             * Class Declaration.
             * sub from package
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
                _package.add(_class);
                return true;
            }
            
            /*
             * method declaration
             * sub from class
            */
            @Override
            public boolean visit(MethodDeclaration node)
            {
                _method = new DefaultMutableTreeNode(node.getName());
                _class.add(_method);
                return true;
            }
            
            /*
             * field declaration
             * sub from class
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
             * sub from method
            */
            @Override
            public boolean visit(VariableDeclarationFragment node) 
            {
                if(node.getParent() instanceof FieldDeclaration)
                    _class.add(new DefaultMutableTreeNode(node.toString()));
                else
                    _method.add(new DefaultMutableTreeNode(node.toString()));
                return false; // do not continue to avoid usage info
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
                return false;
            }
            
            @Override
            public boolean visit(SuperMethodInvocation node)
            {
                _methodCall=new DefaultMutableTreeNode(node);
                _method.add(_methodCall);
                return false;
            }
            
            @Override
            public boolean visit(ReturnStatement node)
            {
                _method.add(new DefaultMutableTreeNode(node.toString()));
                return false;
            }
            
            @Override
            public boolean visit(IfStatement node)
            {
                String elseExist="";
                _if=new DefaultMutableTreeNode("if ("+node.getExpression()+")");
                _method.add(_if);
                enclose(node.getThenStatement().toString(),_if);
                elseExist=node.getElseStatement()+"";
                if(!(elseExist.equals("")))
                {
                    _else=new DefaultMutableTreeNode("else");
                    _method.add(_else);
                    enclose(node.getElseStatement().toString(),_else);
                }
                return false;
            }
            
            @Override
            public boolean visit(EnhancedForStatement node)
            {
                _for=new DefaultMutableTreeNode("for ("+node.getParameter()+" : "+node.getExpression()+")");
                _method.add(_for);
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
                enclose(node.getBody().toString(),_for);
                return false;
            }
            
            @Override
            public boolean visit(WhileStatement node)
            {
                _while=new DefaultMutableTreeNode("while "+node.getExpression());
                _method.add(_while);
                enclose(node.getBody().toString(),_while);
                return false;
            }
            
            @Override
            public boolean visit(DoStatement node)
            {
                _do=new DefaultMutableTreeNode("do-while("+node.getExpression()+")");
                _method.add(_do);
                enclose(node.getBody().toString(),_do);
                return false;
            }
            
            @Override
            public boolean visit(TryStatement node)
            {
                String ada="";
                _try=new DefaultMutableTreeNode("try");
                _method.add(_try);
                enclose(node.getBody().toString(),_try);
                ada=node.getFinally()+"";
                if(!(ada.equals("")))
                {
                    _final=new DefaultMutableTreeNode("finally");
                    _method.add(_final);
                    enclose(node.getFinally().toString(),_final);
                }
                return false;
            }
            
            @Override
            public boolean visit(CatchClause node)
            {
                _catch=new DefaultMutableTreeNode("catch ("+node.getException()+")");
                _method.add(_catch);
                enclose(node.getBody().toString(),_catch);
                return false;
            }
        });
        
        model = new DefaultTreeModel(_package);
        codeTree.setModel(model);
        for (int i = 0; i < codeTree.getRowCount(); i++)
            codeTree.expandRow(i);
    }
    
    private void enclose(String body,DefaultMutableTreeNode parentNode)
    {
        ASTParser parser = ASTParser.newParser(AST.JLS3);
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
                        parentNode.add(new DefaultMutableTreeNode(node+""));
                    }
                    return true;
                }

                @Override
                public boolean visit(EnhancedForStatement node)
                {
                    _for=new DefaultMutableTreeNode("for ("+node.getParameter()+" : "+node.getExpression()+")");
                    parentNode.add(_for);
                    enclose(node.getBody().toString(),_for);
                    return false;
                }
                @Override
                public boolean visit(ForStatement node)
                {
                    _for=new DefaultMutableTreeNode(node.toString());
                    parentNode.add(_for);
                    enclose(node.getBody().toString(),_for);
                    return false;
                }

                @Override
                public boolean visit(IfStatement node)
                {
                    String elseExist="";
                    _if=new DefaultMutableTreeNode("if ("+node.getExpression()+")");
                    parentNode.add(_if);
                    enclose(node.getThenStatement().toString(),_if);
                    elseExist=node.getElseStatement()+"";
                    if(!(elseExist.equals("")))
                    {
                        _else=new DefaultMutableTreeNode("else");
                        parentNode.add(_else);
                        enclose(node.getElseStatement().toString(),_else);
                    }
                    return false;
                }

                @Override
                public boolean visit(VariableDeclarationFragment node) 
                {
                    if(node.getParent() instanceof FieldDeclaration)
                        _class.add(new DefaultMutableTreeNode(node.toString()));
                    else
                        _method.add(new DefaultMutableTreeNode(node.toString()));
                    return false; // do not continue to avoid usage info
                }

                @Override
                public boolean visit(ReturnStatement node)
                {
//                                    tablemodel.addRow(new Object[]{node,cu.getLineNumber(node.getStartPosition())});
                    parentNode.add(new DefaultMutableTreeNode(node+""));
                    return false;
                }

                @Override
                public boolean visit(SuperConstructorInvocation node)
                {
                    _constructorCall=new DefaultMutableTreeNode(node);
                    parentNode.add(_constructorCall);
                    return false;
                }

                @Override
                public boolean visit(MethodInvocation node)
                {
                    _methodCall=new DefaultMutableTreeNode(node);
                    parentNode.add(_methodCall);
                    return true;
                }

                @Override
                public boolean visit(SuperMethodInvocation node)
                {
                    _methodCall=new DefaultMutableTreeNode(node);
                    parentNode.add(_methodCall);
                    return false;
                }

                @Override
                public boolean visit(WhileStatement node)
                {
                    _while=new DefaultMutableTreeNode("while "+node.getExpression());
                    parentNode.add(_while);
                    enclose(node.getBody().toString(),_while);
                    return false;
                }

                @Override
                public boolean visit(DoStatement node)
                {
                    _do=new DefaultMutableTreeNode("do-while("+node.getExpression()+")");
                    parentNode.add(_do);
                    enclose(node.getBody().toString(),_do);
                    return false;
                }

                @Override
                public boolean visit(TryStatement node)
                {
                    String ada="";
                    _try=new DefaultMutableTreeNode("try");
                    parentNode.add(_try);
                    enclose(node.getBody().toString(),_try);
                    ada=node.getFinally()+"";
                    if(!(ada.equals("")))
                    {
                        _final=new DefaultMutableTreeNode("finally");
                        parentNode.add(_final);
                        enclose(node.getFinally().toString(),_final);
                    }
                    return false;
                }

                @Override
                public boolean visit(CatchClause node)
                {
                    _catch=new DefaultMutableTreeNode("catch ("+node.getException()+")");
                    parentNode.add(_catch);
                    enclose(node.getBody().toString(),_catch);
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
