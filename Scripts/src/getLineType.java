import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getLineType {
    public static int ID = 0;
    public static ArrayList<String> getStatementType(String path, ArrayList<String> x) throws Exception{//, ArrayList<String> x
        ArrayList<String> results= new ArrayList<>();
        for (int i = 0 ;i< x.size() ;i++) {
            results.add("class org.eclipse.jdt.core.dom.classDeclaration");
        }
        File candidate = new File(path);
        String absolutePath = candidate.getAbsolutePath();
        System.out.println(absolutePath);
        String dirPath = absolutePath.substring(0,absolutePath.lastIndexOf("\\"));

        System.out.println(dirPath);

        Map<Integer, dataNode> candidateNodes = new HashMap<>();
        CompilationUnit unit = getCompilationUnit(absolutePath,candidate.getName(),dirPath);
        ID =0;
        getDirectChildren(unit,0,candidateNodes);

        for(int i=0;i<candidateNodes.size();i++){
            dataNode nd = candidateNodes.get(i);
            int begin =-1;
            int end =-1;

            if(nd.nodeType.contains("ForStatement")
                    ||nd.nodeType.contains("IfStatement")
                    ||nd.nodeType.contains("EnhancedForStatement")
                    ||nd.nodeType.contains("DoStatement")
                    ||nd.nodeType.contains("SwitchCase")
                    ||nd.nodeType.contains("WhileStatement")
                    ||nd.nodeType.contains("TryStatement")
            ) {
                begin = unit.getLineNumber(nd.node.getStartPosition());


                for(int tmp=0;tmp< nd.childrenLables.size();tmp++) {
                    dataNode ndc = candidateNodes.get(nd.childrenLables.get(tmp));
                    if(ndc.nodeType.contains("Block")||
                            ndc.nodeType.contains("Statement")||
                            ndc.nodeType.contains("inr")||
                            ndc.nodeType.contains("SuperConstructorInvocation")||
                            ndc.nodeType.contains("SwitchCase")
                    ) {
                        end = Math.max(unit.getLineNumber(ndc.node.getStartPosition())-1,
                                unit.getLineNumber(	candidateNodes.get(ndc.label-1).node.getStartPosition()+candidateNodes.get(ndc.label-1).node.getLength())
                        );
                        end = Math.max(begin, end);
                        break;
                    }
                }
            }
            else if(nd.nodeType.contains("Statement")||
                    nd.nodeType.contains("ImportDeclaration")||
                    nd.nodeType.contains("ConstructorInvocation")||
                    nd.nodeType.contains("SuperConstructorInvocation")||
                    nd.nodeType.contains("SwitchCase")   ||
                    nd.nodeType.contains("FieldDeclaration")||
                    nd.nodeType.contains("ClassBodyDeclaration ")||(nd.nodeType.contains("PackageDeclaration"))|| (nd.nodeType.contains("AnonymousClassDeclaration"))||
                    nd.nodeType.contains("MethodDeclaration")
            ) {
                begin = unit.getLineNumber(nd.node.getStartPosition());
                end = unit.getLineNumber(nd.node.getStartPosition()+nd.node.getLength());
            }
            if(begin == -1 || end == -1) {
                continue;
            }
            for (int j = begin;j<=end ;j++) {
                results.set(j-1, nd.nodeType);
            }

        }
        return results;
    }


    public static CompilationUnit getCompilationUnit(String javaFilePath, String javaName, String fileDir) throws Exception{
        File javaFile = new File(javaFilePath);
        String source = FileUtils.readFileToString(javaFile);
        Document document = new Document(source);

        Map<String,String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);

        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        astParser.setSource(document.get().toCharArray());
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setEnvironment(
//               new String[] {"C:\\Program Files\\Java\\jdk1.8.0_121\\jre\\lib"},
                new String[]{"C:/Program Files/Java/jdk1.8.0_301/jre/lib"},
                new String[] {fileDir}, new String[]{"UTF-8"},true);
        astParser.setBindingsRecovery(true);//"/usr/jdk/jre/lib"
        astParser.setResolveBindings(true);
        astParser.setStatementsRecovery(true);
        astParser.setBindingsRecovery(true);
        astParser.setUnitName(javaName);
        astParser.setCompilerOptions(options);
        CompilationUnit compilatoinUnit = (CompilationUnit)(astParser.createAST(null));
        compilatoinUnit.recordModifications();

        return compilatoinUnit;
    }

    public static void getDirectChildren(ASTNode node, int label, Map<Integer,dataNode> Nodes){
        dataNode myNode = new dataNode();
        Nodes.put(label,myNode);
        myNode.label = label;
        myNode.node = node;
        myNode.numberOfToken = getNumberOfTokens(node);
        myNode.nodeType = node.getClass().toString();
        myNode.attachedStatementType = getAttachedStatementInformation(node);

        getContraolInformation(myNode);
        List listProperty = node.structuralPropertiesForType();


        boolean hasChildren = false;
        for(int i = 0; i < listProperty.size(); i++){
            StructuralPropertyDescriptor propertyDescriptor = (StructuralPropertyDescriptor) listProperty.get(i);
            if(propertyDescriptor instanceof ChildListPropertyDescriptor){//ASTNode�б�
                ChildListPropertyDescriptor childListPropertyDescriptor = (ChildListPropertyDescriptor)propertyDescriptor;
                Object children = node.getStructuralProperty(childListPropertyDescriptor);
                List<ASTNode> childrenNodes = (List<ASTNode>)children;
                for(ASTNode childNode: childrenNodes){

                    if(childNode == null)
                        continue;
                    hasChildren = true;
                    myNode.childrenNodes.add(childNode);
                    myNode.childrenLables.add((++ID));
                    getDirectChildren(childNode, ID, Nodes);
                }

            }else if(propertyDescriptor instanceof ChildPropertyDescriptor){//һ��ASTNode
                ChildPropertyDescriptor childPropertyDescriptor = (ChildPropertyDescriptor)propertyDescriptor;
                Object child = node.getStructuralProperty(childPropertyDescriptor);
                ASTNode childNode = (ASTNode)child;
                if(childNode == null)
                    continue;
                hasChildren = true;
                myNode.childrenNodes.add(childNode);
                myNode.childrenLables.add((++ID));
                getDirectChildren(childNode, ID, Nodes);
            }
        }
        if(hasChildren){
            myNode.isLeaf = false;
            int cnt = 0;
            for(Integer it: myNode.childrenLables){
                dataNode dataNode = Nodes.get(it);
                cnt += dataNode.numberOfToken2;
            }
            myNode.numberOfToken2 = cnt;
        }
        else{
            myNode.isLeaf = true;
            myNode.numberOfToken2 = 1;
        }

    }

    public static int getNumberOfTokens(ASTNode node){
        String string = node.toString();
        int cnt = 0;
        for(int i=0; i<string.length();i++){
            int value = Integer.valueOf(string.charAt(i));
            if(value >=1 && value <=127){
                cnt++;
            }
        }
        return cnt;
    }

    public static String getAttachedStatementInformation(ASTNode node){
        String result = getParentStatementInformation(node);
        if(!result.equals("null")){
            return result;
        }
        ASTNode tp = node;
        while(tp.getParent() != null &&!(tp.getParent() instanceof CompilationUnit)){
            tp = tp.getParent();
            result = getParentStatementInformation(tp);
            if(!result.equals("null")){
                return result;
            }
        }
        return "null";
    }

    public  static String getParentStatementInformation(ASTNode node){
        if((node instanceof AssertStatement) || (node instanceof BreakStatement)
                || (node instanceof ConstructorInvocation) || (node instanceof ContinueStatement)
                || (node instanceof DoStatement) || (node instanceof EmptyStatement)
                || (node instanceof EnhancedForStatement) || (node instanceof ExpressionStatement)
                || (node instanceof ForStatement) || (node instanceof IfStatement)
                || (node instanceof LabeledStatement) || (node instanceof ReturnStatement)
                ||  (node instanceof SuperConstructorInvocation) || (node instanceof SwitchCase)
                ||  (node instanceof SwitchStatement) || (node instanceof SynchronizedStatement)
                || (node instanceof ThrowStatement) || (node instanceof TryStatement)
                || (node instanceof TypeDeclarationStatement) || (node instanceof VariableDeclarationStatement) ||(node instanceof PackageDeclaration)
                || (node instanceof WhileStatement )){
            return node.getClass().toString();
        }
        return "null";
    }

    public static void getContraolInformation(dataNode dataNode){
        ASTNode ansNode = null;
        ASTNode tp = dataNode.node;

        while(tp.getParent() != null && !(tp.getParent() instanceof CompilationUnit)){
            tp = tp.getParent();
            if(tp instanceof IfStatement){
                dataNode.logicPoseList.add(1);
            }
            else if(tp instanceof EnhancedForStatement){
                dataNode.logicPoseList.add(2);

            }else if(tp instanceof ForStatement){
                dataNode.logicPoseList.add(3);
            }
            else if(tp instanceof DoStatement){
                dataNode.logicPoseList.add(4);
            }
            else if(tp instanceof WhileStatement){
                dataNode.logicPoseList.add(5);
            }
        }
        dataNode.logicPoseList.add(6);
    }


    public static void getType(String projectName, int bugid, String version, int fileid) throws Exception {
        String codePath = "./afterGenerationEntropy/"+projectName+"/output_"+projectName+"_"+bugid+"_"+version+"/"+fileid+".code.java.lines";
        String entropyPath = "./afterGenerationEntropy/"+projectName+"/"+projectName+"_"+bugid+"_"+version+"_entropy/"+fileid+"/entropy.txt";
        File file1 = new File("./afterGenerationEntropy/"+projectName+"/"+projectName+"_"+bugid+"_"+version+"_entropy/"+fileid+"/lineEntropyType.txt");
        if(file1.exists()){
            file1.delete();
        }
        ArrayList<String> entropy = getFileNO.readFile(entropyPath);
        ArrayList<String> path1 = getFileNO.readFile(codePath);
        ArrayList<String> types = getStatementType(codePath,path1);
//        System.out.println(types.size());

        for(int i=0;i< entropy.size();i++){
            String type = types.get(i);
            int pos =types.get(i).lastIndexOf(".");
            String t = type.substring(pos+1);
            getFileNO.appendFile(entropy.get(i) + "," + t+"\n","./afterGenerationEntropy/"+projectName+"/"+projectName+"_"+bugid+"_"+version+"_entropy/"+fileid+"/lineEntropyType.txt");
        }
    }



    public static void main(String[] args) throws Exception {
        System.out.println("----start----");
        String projectName=setting.projectName;//"Ognl";
        int[] bugids = setting.bugids;
        String[] versions = new String[]{"free","buggy","fix"};//"free","buggy","fix"

        for(int k=0;k<bugids.length;k++){
            int bugid = bugids[k];


            for(int tt=0;tt<versions.length;tt++){
                String version = versions[tt];
                int num = getOriginalLineEntropy.getFileNumer(projectName,bugid,version);

                    for(int i=0;i<num;i++){
                        try{
                            getType(projectName,bugid,version,i);
                            System.err.println(i + "::"+num);
                            System.out.println("finished " + i);
                        }catch(Exception e){

                        }
                    }
            }
        }
    }
}
