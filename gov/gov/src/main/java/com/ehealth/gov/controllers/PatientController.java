package com.ehealth.gov.controllers;

import com.ehealth.gov.models.PatientModel;
import com.ehealth.gov.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
public class PatientController {

    @Autowired
    PatientRepository patientRepository;
    PatientModel patientModels = new PatientModel();
    List<PatientModel>  patientModelList = new ArrayList<PatientModel>();

    @PostMapping(path = "/patientEntry", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public HashMap addPatients(@RequestBody PatientModel patientModel){

        System.out.println("Patient Name : "+patientModel.getPatientName());
        System.out.println("Patient Address : "+patientModel.getPatientAddress());
        System.out.println("Patient Phone : "+patientModel.getPatientPhone());
        //saving data to database
        patientRepository.save(patientModel);
        //List<PatientModel> list = new ArrayList<PatientModel>();
        //list.add(patientModel);

        patientModels = patientModel;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", "success");
        hashMap.put("patient",patientModel);
        return hashMap;
    }

    //Usage of Hashmaps
    //to get response as { "status" : "success"}
    @PostMapping(path = "/addPatients",consumes = "application/json",produces = "application/json")
    @ResponseBody
    public HashMap addPatientsData(@RequestBody PatientModel patientModel)
    {
        System.out.println("Name :"+patientModel.getPatientName());
        System.out.println("Phone number :"+patientModel.getPatientPhone());

        List<PatientModel> pateientModelList = new ArrayList<>();
        pateientModelList.add(patientModel);
        HashMap<String,Object> patientAddMap = new HashMap<>();
        patientAddMap.put("Status","success");
        patientAddMap.put("PatientRecord",patientModel);//passing patient model
        return patientAddMap;

    }


    //To get Dynamically adding values using the previous method
    @GetMapping(path = "/viewData")
    public HashMap viewData()
    {
        HashMap hashMap = new HashMap<>();
        hashMap.put("status","success");
        hashMap.put("PatientRecord",patientRepository.findAll());
        return hashMap;

    }

    @GetMapping(path = "/view", produces = "application/json")
    @ResponseBody
    public HashMap viewPatients(){
        //PatientModel patientModel = new PatientModel();
        //List<PatientModel> list = new ArrayList<PatientModel>();
        //list.add(patientModel);
        //patientModelList.add(patientModels);
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("status", "success");
        hashMap.put("data",patientRepository.findAll());
        return hashMap;
    }

    @PostMapping(path = "/searchUsingPhone",consumes = "application/json",produces = "application/json")
    public  HashMap searchUsingPhone(@RequestBody PatientModel requestNumber)
    {
        int found = 0;
        System.out.println("the phone number"+requestNumber.getPatientPhone());
        PatientModel pateientModel = new PatientModel();
        for (PatientModel model: patientModelList)
        {
            System.out.println("model"+model.getPatientPhone());
            if (model.getPatientPhone().equals(requestNumber.getPatientPhone()))
            {
                found = 1;
                pateientModel = model;
                break;
            }
            else
            {

            }
        }
        HashMap<String,Object>hashMap = new HashMap<>();
        if (found == 1)
        {
            hashMap.put("status","success");
            hashMap.put("Patient",pateientModel);
        }
        else
        {
            hashMap.put("status","failed");

        }
        return  hashMap;

    }

    //Returning data as list: outpu look like [{values},{values}]

    @GetMapping(path = "/viewPatientList", produces = "application/json" )
    public List<PatientModel> viewpatientList()
    {
        List<PatientModel>patientlist = (List<PatientModel>) patientRepository.findAll();
        return patientlist;


    }

    //Using Native Queries
    //select Query
    @GetMapping(path = "/viewPatientLists", produces = "application/json")
    public List viewPatientListUsingNativeQuery()
    {
        List<PatientModel>patientlist = patientRepository.getPatientList();
        return patientlist;

    }

    @PostMapping(path="/searchPatient", consumes ="application/json", produces = "application/json" )
    public HashMap searchPatient(@RequestBody PatientModel patientModel){
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if(patientModel.getPatientPhone()!=null){
            boolean flag = false;
            for (PatientModel model:patientModelList
                 ) {
                if(model.getPatientPhone().equals(patientModel.getPatientPhone())){
                    hashMap.put("status", "Success");
                    hashMap.put("data",model);
                    flag=true;
                }
            }
            if(!flag){
                hashMap.put("status", "Failed");
                hashMap.put("data", "Patient Not Found");
            }

        }else{
            hashMap.put("status", "Invalid Mobile Number");
        }

        return hashMap;
    }


    @PostMapping(path="/deletePatientRecord", consumes ="application/json", produces = "application/json" )
    public HashMap deletePatientUsingPhone(@RequestBody PatientModel patientModel){
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if(patientModel.getPatientPhone()!=null){
            boolean flag = false;
            for (PatientModel model:patientModelList) { // for(int 1=0;i<patientModelList.size();i++)
                if(model.getPatientPhone().equals(patientModel.getPatientPhone())){
                    patientModelList.remove(model);
                    hashMap.put("status", "Successfully Deleted");
                    flag=true;
                }
            }
            if(!flag){
                hashMap.put("status", "Patient Entry Not Found");
            }

        }else{
            hashMap.put("status", "Invalid Mobile Number");
        }

        return hashMap;
    }

    //Select Query using ?
    @PostMapping(path = "/searchPatientListByPhone",consumes = "application/json",produces = "application/json")
    public List searchPatientListByPhone(@RequestBody PatientModel model)
    {
        List<PatientModel>patientlist = patientRepository.searchPatientByPhone(model.getPatientPhone());
        return patientlist;


    }

    @PostMapping(path = "/searchPatientData",consumes = "application/json",produces = "application/json")
    public HashMap searchPatientUsingPhone(@RequestBody PatientModel model)
    {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if(model.getPatientPhone()!=null){
            PatientModel patientModel = patientRepository.searchPatient(model.getPatientPhone());
            if(patientModel!=null){
              hashMap.put("status", "success");
              hashMap.put("data",patientModel);
            }
            else{
                hashMap.put("status", "Failed");
                hashMap.put("data", "Patient Not Found");
            }

        }else{
            hashMap.put("status", "Invalid Mobile Number");
        }
        return hashMap;
    }

    //Search Query using  @Param annotation
    @PostMapping(path =  "/multiSearchPatient",consumes = "application/json",produces = "application/json")
    public  HashMap searchUsingNameAndPhone(@RequestBody PatientModel model)
    {
        List<PatientModel> list = patientRepository.searchPatientByNameAndPhone(model.getPatientPhone(),model.getPatientName());

        HashMap<String, Object> map = new HashMap<>();
        if(list.isEmpty())
        {
            map.put("Status","Not Found");
        }
        else
        {
            map.put("Status","Found");
            map.put("Data",list);

        }
        return  map;
    }

    //Delete Query
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/deletePatient",consumes = "application/json",produces = "application/json")
    public  HashMap deletePatient(@RequestBody PatientModel model)
    {
        int value = patientRepository.deleteAPatient(model.getId());
        System.out.println(value);
        HashMap<String, String>map = new HashMap<>();

        if (value == 1)
        {
            map.put("message","Deleted successfully");
            map.put("status","success");

        }
        else {
            map.put("message","Delete Failed");
            map.put("status","failed");

        }

        return map;

    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/updatePatient",consumes = "application/json",produces = "application/json")
    public  HashMap updatePatient(@RequestBody PatientModel model)
    {
        int value = patientRepository.updatePatientDetails(model.getId(),model.getPatientName(),model.getPatientAddress(),model.getPatientPhone());
        HashMap<String,String> map = new HashMap<>();
        if (value == 1)
        {
            map.put("message","Updated Successfully");
            map.put("status","success");
        }
        else
        {
            map.put("message","Update Failed");
            map.put("status","failed");

        }
        return  map;
    }


}
