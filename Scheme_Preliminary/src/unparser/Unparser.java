package unparser;

import scheme_ast.*;
import util.Pair;

import java.util.*;

public class Unparser {
  
  public static String unparse(Expression ast)
  {
    return unparse(ast,new Stack<Integer>());
  }
  
  public static String unparse(Expression ast, Stack<Integer> indent_length) {
   String result="";
   
   if(ast instanceof IntExpression)
   {
     result+=((Integer)((IntExpression)ast).getValue()).toString()+" ";
   }
   
   else if(ast instanceof IdExpression)
   {
     result+=((IdExpression)ast).getId()+" ";
   }
   
   else if(ast instanceof CallExpression)
   {
     result+="("+unparse(((CallExpression)ast).getOperator(),indent_length);
     for(Expression expression:((CallExpression)ast).getOperands())
     {
       result+=(unparse(expression,indent_length));
       
     }
     result+=")";
   }
   
   else if(ast instanceof IfExpression)
   {
     String indent_space="";
     if(indent_length.isEmpty()==true)
       indent_length.push(("(if ").length());
     else
     {
       indent_length.push(indent_length.peek()+("(if ").length());
     }
     
     //System.out.printf("indent_length=%d\n",indent_length.peek());
     for(int i=0;i<indent_length.peek();i++)
       indent_space+=" ";
     
     result+="(if "+unparse(((IfExpression)ast).getCondition(),indent_length)+"\n"
               +indent_space+unparse(((IfExpression)ast).getThen(),indent_length)+"\n"
               +indent_space+unparse(((IfExpression)ast).getElse(),indent_length)+" )";
     indent_length.pop();
   }
   
   else if(ast instanceof LambdaExpression)
   {
     String indent_space="";
     if(indent_length.isEmpty()==true)
       indent_length.push(("(lambda ").length());
     else
       indent_length.push(indent_length.peek()+("(lambda ").length());
     for(int i=0;i<indent_length.peek();i++)
       indent_space+=" ";
     
     result+="(lambda ( ";
     for(String s:((LambdaExpression)ast).getParameters())
     {
       result+=s+" ";
     }
     result+=") \n";
     result+=indent_space+"\n"
             +indent_space+unparse(((LambdaExpression)ast).getBody(),indent_length)+" )"
             +" \n";
     indent_length.pop();
   }
   
   else if(ast instanceof LetExpression)
   {
     String indent_space="";
     if(indent_length.isEmpty()==true)
       indent_length.push(("(let ").length());
     else
       indent_length.push(indent_length.peek()+("(let ").length());
     
     for(int i=0;i<indent_length.peek();i++)
       indent_space+=" ";
     
     result+="(let (\n";
     
     for(Pair<String, Expression> i :((LetExpression)ast).getBindings())
     {
       result+=indent_space+" ( " + i.first +" ";
       result+=unparse(i.second,indent_length)+" )\n";
     }
     
     result+=indent_space+")\n"+indent_space+unparse(((LetExpression)ast).getBody(),indent_length)+" )";
     indent_length.pop();
   }
 

   return result;
 }
 /*
 public static void main(String args[])
 {
   Expression simpleIf = new IfExpression(
   new CallExpression(
     new IdExpression("<"),
     new ArrayList<Expression>(
       Arrays.asList(
        new IntExpression(3),
        new IntExpression(4)))),
   new CallExpression(
     new IdExpression("*"),
     new ArrayList<Expression>(
       Arrays.asList(
         new IntExpression(3),
         new IntExpression(4)))),
   new CallExpression(
     new IdExpression("-"),
     new ArrayList<Expression>(
       Arrays.asList(
         new IntExpression(18),
         new IntExpression(7)))));
         System.out.println(unparse(simpleIf));
         HashMap<String, Expression> xBindings = new HashMap<String, Expression>();
         Expression xThree = new IntExpression(3);
         Expression xTimes = new CallExpression(
           new IdExpression("*"),
           new ArrayList<Expression>(
             Arrays.asList(
               new IntExpression(5),
               new IntExpression(6))));
 xBindings.put("x", xThree);
 xBindings.put("y", xTimes);
 Expression xX = new IdExpression("x");
 Expression xY = new IdExpression("y");
 Expression xPlus = new IdExpression("+");
 Expression xLetBody = new CallExpression(xPlus, 
   new ArrayList<Expression>(
      Arrays.asList(xX, xY)));

 Expression simpleLet = new LetExpression(xBindings, xLetBody);
 System.out.println(unparse(simpleLet));
 ArrayList<String> parameter=new ArrayList<String>();
 parameter.add("x");
 Expression simpleLa=new LambdaExpression(parameter,simpleLet);
 System.out.println(unparse(simpleLa));
 }*/
 
}
