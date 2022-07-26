import java.io.File;
import java.util.ArrayList;

public class getChangeLine {


    public static void getDiffFile(String projectName){
        String path = "./"+projectName+"/diffInfo";
        File file = new File(path);
        File[] files = file.listFiles();

        for(File f : files){
            String fileName = f.getName();
//            System.out.println(fileName);
            if(fileName.endsWith("v12.txt")){
                String before = getDeleteChange(fileName,"free");
                if(!before.equals("")){
                    System.out.println("v12: before: " + before);
                    getFileNO.appendFile(before+"\n","./AChangeLine/v12.txt");
                }
                String after = getAddChange(fileName,"buggy");
                if(!after.equals("")){
                    System.out.println("v12: after: " + after);
                    getFileNO.appendFile(after+"\n","./AChangeLine/v12.txt");
                }
            }
            //-------------------------------------------

            if(fileName.endsWith("v13.txt")){
                String before = getDeleteChange(fileName,"free");
                if(!before.equals("")){
                    System.out.println("v13: before: " + before);
                    getFileNO.appendFile(before+"\n","./AChangeLine/v13.txt");
                }
                String after = getAddChange(fileName,"fix");
                if(!after.equals("")){
                    System.out.println("v13: after: " + after);
                    getFileNO.appendFile(after+"\n","./AChangeLine/v13.txt");
                }
            }

            //---------------------------------

            if(fileName.endsWith("v23.txt")){
                String before = getDeleteChange(fileName,"buggy");
                if(!before.equals("")){
                    System.out.println("v23: before: " + before);
                    getFileNO.appendFile(before+"\n","./AChangeLine/v23.txt");
                }
                String after = getAddChange(fileName,"fix");
                if(!after.equals("")){
                    System.out.println("v23: after: " + after);
                    getFileNO.appendFile(after+"\n","./AChangeLine/v23.txt");
                }
            }


        }
    }

    public static String getDeleteFileNo(String fileName, String version){
        int pos = fileName.indexOf(".");
        String fName = fileName.substring(0,pos);
        String[] parts = fName.split("@");
        String res="";
        String part1 = parts[0]; //projectName
        String part2 = parts[1]; // bugid
        String part3 = parts[2]; // freeFile
        String part4 = parts[3]; // buggyFile
        if(version.equals("free")){
            res = part1 +","+part2+","+version+ ","+ part3;
        }else{
            res = part1 +","+part2+","+version+","+part4;
        }

        return res;
    }

    public static ArrayList<String> getFileCtx(String fileName){
        int pos = fileName.indexOf(".");
        String fName = fileName.substring(0,pos);
        String[] parts = fName.split("@");
        String projectName= parts[0]; // projectName
        String path = "./"+projectName+"/diffInfo/"+fileName;
        ArrayList<String> ctx = getFileNO.readFile(path);
        return ctx;
    }

    public static ArrayList<String> cleanDeleteFileCtx(ArrayList<String> ctx){
        ArrayList<String> res = new ArrayList<>();
        for(int i=5;i< ctx.size()-1;i++){
            String eachLine = ctx.get(i);
            if(!(eachLine.trim().startsWith("+"))){
                res.add(eachLine);
            }
        }
        return res;
    }

    public static String getDeleteChange(String fileName,String version){
        String res="";
        String freeFileNo = getDeleteFileNo(fileName,version);

        ArrayList<String> contex = getFileCtx(fileName);
        if(contex.size() == 0){
            return "";
        }else{
            ArrayList<String> afterClean = cleanDeleteFileCtx(contex);
            ArrayList<Integer> index= new ArrayList<>();
            for(int i=0;i<afterClean.size();i++){
                String line = afterClean.get(i).trim();
                if(line.startsWith("-")){
                    index.add(i+1);
//                System.out.println(line);
                }
            }
//        System.out.println(freeFileNo+","+index);
            res = freeFileNo+","+index;
            return res;
        }
    }

    public static String getAddFileNo(String fileName, String version){
        int pos = fileName.lastIndexOf("_");
        String fName = fileName.substring(0,pos);
        String[] parts = fName.split("@");
        String res="";
        String part1 = parts[0]; //projectName
        String part2 = parts[1]; // bugid
        String part3 = parts[3]; // buggyFile
        String part4 = parts[4]; // fixFile
        if(version.equals("buggy")){
            res = part1 +","+part2+","+version+ ","+ part3;
        }else{
            res = part1 +","+part2+","+version+","+part4;
        }

        return res;
    }

    public static ArrayList<String> cleanAddFileCtx(ArrayList<String> ctx){
        ArrayList<String> res = new ArrayList<>();
        for(int i=5;i< ctx.size()-1;i++){
            String eachLine = ctx.get(i);
            if(!(eachLine.trim().startsWith("-"))){
                res.add(eachLine);
            }
        }
        return res;
    }


    public static String getAddChange(String fileName,String version){
        String res="";
        String freeFileNo = getAddFileNo(fileName,version);

        ArrayList<String> contex = getFileCtx(fileName);
        if(contex.size() == 0){
            return "";
        }else{
            ArrayList<String> afterClean = cleanAddFileCtx(contex);
            ArrayList<Integer> index= new ArrayList<>();
            for(int i=0;i<afterClean.size();i++){
                String line = afterClean.get(i).trim();
                if(line.startsWith("+")){
                    index.add(i+1);
//                System.out.println(line);
                }
            }

            res = freeFileNo+","+index;
            return res;
        }
    }

    public static void main(String[] args){
        System.out.println("--------start---------------");
//        getFileNo();
        String[] projectNames = new String[]{"Dbutils","Functor","Imaging","IO","JXR","MShade","Validator",
                "Pool","Email","Graph","Net","Numbers_angle","MGpg","Text","Shiro_core","Jena_core","Shiro_web",
                "MDeploy","Jackrabbit_filevault_vault_validation","Jackrabbit_oak_core","Doxia_module_apt",
                "Xmlgraphics","Rdf_jena","Maven_checkstyle_plugin","James_project_core","Pdfbox_fontbox",
                "AaltoXml","HttpClient5","jackson_modules_java8_datetime","Pdfbox_pdfbox","Storm_client","James_mime4j_core",
                "JacksonDataformatsText_yaml","JacksonDataformatsText_properties","JacksonDataformatBinary_avro",
                "JacksonDataformatBinary_cbor","JavaClassmate","JacksonModuleJsonSchema","JacksonDatatypeJoda",
                "Bcel","JacksonDataformatBinary_protobuf","Jackrabbit_filevault_vault_core","JacksonDatatypeJsr310",
                "JacksonDataformatBinary_smile","JacksonModuleAfterburner","Woodstox","MetaModel_core","MetaModel_csv",
                "MetaModel_excel","MetaModel_jdbc","MetaModel_pojo","MetaModel_salesforce","Wink_common","Xbean_naming",
                "James_project_server_container_core","Johnzon_core","Nifi_mock","Rat_core","Rat_plugin","Tez_common",
                "Tinkerpop_gremlin_core","Webbeans_web","Hono_client","Httpcomponents_core_h2","Httpcomponents_core_httpcore5",
                "Johnzon_jsonb","Johnzon_jaxrs","Hbase_common","Incubator_tamaya_api","James_project_mailet_standard","Johnzon_jsonschema",
                "Johnzon_mapper","Karaf_main","Appformer_uberfire_commons_editor_backend","Kie_pmml_commons","Kie_memory_compiler",
                "Jbpm_human_task_workitems","Drools_traits","Drools_model_compiler","Appformer_uberfire_security_management_client","Appformer_uberfire_workbench_client",
                "Deltaspike_api","Flume_ngcore","Jandex","Ognl","Qpid_client","Switchyard_admin","Weld_se_core","Jboss_modules",
                "Jboss_threads","Minaftp_api","Sling_validation","Switchyard_config","Switchyard_validate","Vysper_nbxml",
                "Wildfly_naming_client","Dosgi_common","Fluo_api","Hivemall_core","Knox_assertion_common","Oozie_client",
                "Qpidjms_client","Rdf4j_query","Rdf4j_rio_api","Rdf4j_rio_jsonld","Rdf4j_rio_rdfjson","Rdf4j_rio_rdfxml",
                "Rdf4j_rio_turtle","Sentry_ccommon","Sling_apiregions","Sling_cpconverter","Sling_feature","Tiles_api",
                "Tiles_core","Twill_dcore","Maven2_artifact","Maven2_project","Math","Wicket_request","Rave_web","Rave_commons",
                "Rave_core","Mrunit","Xbean_reflect","Shindig_common","Mshared_archiver","Wicket_cdi","Wicket_core","Struts1_core",
                "Cayenne_jms","Cayenne_jgroups","Wicket_spring","Wicket_util","Cayenne_xmpp"};

        for(int i=0;i< projectNames.length;i++){
            getDiffFile(projectNames[i]);
        }

    }

}
