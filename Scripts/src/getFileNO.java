import java.io.*;
import java.util.ArrayList;

public class getFileNO {

    /*
     based on patch, get the correspinding file number after preprocess
     */

    public static ArrayList<String> readFile(String fileName){
        ArrayList<String> result = new ArrayList<String>();
        File file = new File(fileName);
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while((tempString = reader.readLine()) != null)
            {
                if(!tempString.equals("")){
                    result.add(tempString);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public static void appendFile(String line, String path){
        FileWriter fw = null;

        try{
            File f = new File(path);
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter pw = new PrintWriter(fw);
        pw.print(line);
        pw.flush();
        try{
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void getFileNO(String projectName){

        File tempFi = new File("./"+projectName+"/INFO.txt");
        if(tempFi.exists()){
            tempFi.delete();
        }

        String path ="./defects4j/framework/projects/"+projectName+"/patches";
        File folder = new File(path);
        File[] fileList = folder.listFiles();
        ArrayList<ArrayList<String>> fileNos= new ArrayList<>();

        for(File file : fileList){
            String fileName = file.getName();
            String filePath = file.getAbsolutePath();
            if(fileName.endsWith("src.patch")){

               String bugId = fileName.split("\\.")[0];

                ArrayList<String> context = readFile(filePath);
                for(String each : context){
                    if(each.startsWith("diff")){
                        String res = getFile(each);
                        ArrayList<String> fileNO =constructPre(projectName,bugId,res);
//                        System.out.println(fileNO);
                        fileNos.add(fileNO);
                    }
                }
            }
        }
        for(ArrayList<String> e : fileNos){
            for(String temp: e){
                System.out.println(temp);
                appendFile(temp+"\n","./"+projectName+"/INFO.txt");
                appendFile(temp+"\n","./INFO.txt");
            }

        }
    }


    public static String getFile(String line){
        String path = line.split(" ")[2];
        int pos = path.indexOf("/");
        String res = path.substring(pos+1);
        return res;
    }

    public static  ArrayList<String> constructPre(String project, String bugID, String ctx){
        String[] versions = new String[]{"free","buggy","fix"};
        ArrayList<String> numbers = new ArrayList<>();

        for(int i=0;i<versions.length;i++){
            String path = "./"+project+"/output_"+project+"_"+bugID+"_"+versions[i]+"/fileinfo1.txt";

            ArrayList<String> res = readFile(path);
            String number="";
            for(String eachLine : res){
                if(eachLine.contains(ctx)){
                    number = eachLine.split(",")[0];
                    numbers.add(project+","+bugID+","+ versions[i] +","+number);
                }
            }
        }
        return numbers;
    }


    public static void main(String[] args){
        System.out.println("-----start-----");
        String projectName = "Math";
        int bugID =1;
        String version="buggy";

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
         for(int i=0;i<projectNames.length;i++){
             getFileNO(projectNames[i]);
         }
    }
}
