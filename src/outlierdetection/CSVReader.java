package outlierdetection;
import org.joda.time.Instant;

import geohash.GeoHash;
import timehash.TimeHash;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

// $example on$
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
// $example off$




//in this package we will get new categorical data

public class CSVReader {

    public static void main(String[] args) {

        //Varible for reading the csv file
        String current = System.getProperty("user.dir");
        String csvFile = current + "/src/data/in/newyork.csv";
        String line = "";
        String cvsSplitBy = ",";

        //variable to record the content of our csv file

        Integer cmplnt_num;
        Date cmplnt_fr_dt;
        Date cmplnt_fr_tm;
        Date cmplnt_to_dt;
        Date cmplnt_to_tm;
        Date rpt_dt;
        Integer ky_cd;
        String ofns_desc;
        Integer pd_cd;
        String pd_desc;
        String crm_atpt_cptd_cd;
        String law_cat_cd;
        String juris_desc;
        String boro_nm;
        Integer addr_pct_cd;
        String loc_of_occur_desc;
        String prem_typ_desc;
        String parks_nm;
        String hadevelopt;
        Integer x_coord_cd;
        Integer y_coord_cd;
        Double latitude;
        Double longitude;
        String lat_lon;

        //Variables to detect outliers

        //variable of Geo Hash
        GeoHash new_hash;


        //varibles for Hash Time
        String day_night;
        Integer month;
        int dayOfWeek;
        int weekOfYear;
        int weekOfMonth;
        int dayOFMonth;
        int hour;


        //Timhash variable
        String timehash;
        double epochtime;
        

        //variable for conserving new line content
        String newline;


        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            //Reading the first colones names string line
           BufferedWriter bw = new BufferedWriter(new FileWriter(current + "/src/data/out/updatenewyork.csv"));


           /* newline = "CMPLNT_NUM,CMPLNT_FR_DT,CMPLNT_FR_TM,CMPLNT_TO_DT,CMPLNT_TO_TM,RPT_DT=,KY_CD"
                    + ",OFNS_DESC,PD_CD,PD_DESC,CRM_ATPT_CPTD_CD,LAW_CAT_CD,JURIS_DESC,BORO_NM,ADDR_PCT_CD" +
                    ",LOC_OF_OCCUR_DESC,PREM_TYP_DESC,PARKS_NM,HADEVELOPT,X_COORD_CD,Y_COORD_CD,Latitude,Longitude"
                    +",Lat_Lon,GeoHash,Week of year,Month,Day of month,Week of month,Day of week,Hour,Day/Night,TimeHash";*/
            newline="CMPLNT_NUM,GeoHash,Week of year,Month,Day of month,Week of month,Day of week,Hour,Day/Night,TimeHash";
            bw.write(newline);
            bw.newLine();


            br.readLine();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] content = line.split(cvsSplitBy);

                //parsing data in it normal types

                cmplnt_num = Integer.parseInt(content[0]);
                cmplnt_fr_dt = new SimpleDateFormat("MM/dd/yyyy").parse(content[1]);
                cmplnt_fr_tm = new SimpleDateFormat("hh:mm:ss").parse(content[2]);
                cmplnt_to_dt = new SimpleDateFormat("MM/dd/yyyy").parse(content[3]);
                cmplnt_to_tm = new SimpleDateFormat("hh:mm:ss").parse(content[4]);
                rpt_dt = new SimpleDateFormat("MM/dd/yyyy").parse(content[5]);
                ky_cd = Integer.parseInt(content[6]);
                ofns_desc = content[7];
                pd_cd = Integer.parseInt(content[8]);
                pd_desc = content[9];
                crm_atpt_cptd_cd = content[10];
                law_cat_cd = content[11];
                juris_desc = content[12];
                boro_nm = content[13];
                addr_pct_cd = Integer.parseInt(content[14]);
                loc_of_occur_desc = content[15];
                prem_typ_desc = content[16];
                parks_nm = content[17];
                hadevelopt = content[18];
                x_coord_cd = Integer.parseInt(content[19]);
                y_coord_cd = Integer.parseInt(content[20]);
                latitude = Double.parseDouble(content[21]);
                longitude = Double.parseDouble(content[22]);
                lat_lon = content[23] + "," + content[24];



                //Initialising Geohash variable
                new_hash = GeoHash.withCharacterPrecision(latitude, longitude, 7);

                //Hash Time variables initialising
                Date cmplnt_fr = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(content[1] + " " + content[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cmplnt_fr);
                month = calendar.get(Calendar.MONTH);
                hour = calendar.get(Calendar.HOUR);
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
                dayOFMonth = calendar.get(Calendar.DAY_OF_MONTH);
                if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
                    day_night = "PM";
                } else {
                    day_night = "AM";
                }
                //Initialising timehash variables                
                epochtime=cmplnt_fr_dt.toInstant().getEpochSecond();
                timehash=TimeHash.encode(epochtime, 5);
               
                

                System.out.println("[CMPLNT_NUM= " + cmplnt_num + " , CMPLNT_FR_DT=" + cmplnt_fr_dt + " , CMPLNT_FR_TM=" + cmplnt_fr_tm +
                        " , CMPLNT_TO_DT=" + cmplnt_to_dt + " , CMPLNT_TO_TM=" + cmplnt_to_tm + " , RPT_DT=" + rpt_dt + " , KY_CD=" + ky_cd +
                        " , OFNS_DESC=" + ofns_desc + " , PD_CD=" + pd_cd+ " , PD_DESC=" + pd_desc + " , CRM_ATPT_CPTD_CD=" + crm_atpt_cptd_cd +
                        " , LAW_CAT_CD=" + law_cat_cd + " , JURIS_DESC=" + juris_desc + " , BORO_NM=" + boro_nm + " , ADDR_PCT_CD=" + addr_pct_cd +
                        " , LOC_OF_OCCUR_DESC=" + loc_of_occur_desc + " , PREM_TYP_DESC=" + prem_typ_desc + " , PARKS_NM=" + parks_nm +
                        " , HADEVELOPT=" + hadevelopt + " , X_COORD_CD=" + x_coord_cd + " , Y_COORD_CD=" + y_coord_cd + " , Latitude=" + latitude +
                        " , Longitude=" + longitude + " , Lat_Lon=" + lat_lon + " , GeoHash=" + new_hash.toBase32() +
                        ", Week of year= " + weekOfYear + ",Month= " + month + ", Day of month= " + dayOFMonth + ",Week of month= " + weekOfMonth + ",Day of week= " + dayOfWeek +
                        ",Hour= " + hour + ",Day/Night= " + day_night  + ",TimeHash= " + timehash +"]");

                //newline = line + "," + new_hash.toBase32() + "," + weekOfYear + "," + month + "," + dayOFMonth + "," + weekOfMonth + "," + dayOfWeek + "," + hour + "," + day_night+","+timehash;
                newline=  cmplnt_num+","+ new_hash.toBase32() + "," + weekOfYear + "," + month + "," + dayOFMonth + "," + weekOfMonth + "," + dayOfWeek + "," + hour + "," + day_night+","+timehash;
                
                bw.write(newline);
                bw.newLine();
            }
            br.close();
            bw.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}