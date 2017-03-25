package assign5_rsofiov.rsofiov.bsse.asu.edu.assign5_rsofiov;

import org.json.JSONObject;

/**
 * Created by emilybyrne on 3/21/17.
 */

public class Place {
    public String nameP, category, description, addressStreet, addressTitle, image;
    public double lon, lat, ele;
   // public Vector<String> takes;

    Place(){
        this.nameP = nameP;
        category = "";
        description = "";
        addressStreet = "";
        addressTitle = "";
        description = "";
        addressStreet = "";
        addressTitle = "";
        image = "";
        lon = 0;
        lat = 0;
        ele = 0;
        //this.takes.addAll(Arrays.asList(courses));
    }



    Place(String jsonStr){
        try{
            JSONObject jo = new JSONObject(jsonStr);
            nameP = jo.getString("name");
            category = jo.getString("category");
            description = jo.getString("description");
            addressStreet = jo.getString("addressStreet");
            addressTitle = jo.getString("addressTitle");
            lon = jo.getInt("longitude");
            lat = jo.getInt("latitude");
            ele = jo.getInt("elevation");

        }catch (Exception ex){
            System.out.println(this.getClass().getSimpleName()+
                    ": error converting from json string");
        }
    }

    public Place(JSONObject jsonObj){
        try{
            System.out.println("constructor from json received: "+
                    jsonObj.toString());
            nameP = jsonObj.getString("name");
            category = jsonObj.getString("category");
            description = jsonObj.getString("description");
            addressStreet = jsonObj.getString("address-street");
            addressTitle = jsonObj.getString("address-title");
            lon = jsonObj.getDouble("longitude");
            lat = jsonObj.getDouble("latitude");
            ele = jsonObj.getDouble("elevation");

        }catch(Exception ex){
            System.out.println(this.getClass().getSimpleName()+
                    ": error converting from json string");
        }
    }

    public JSONObject toJson(){
        JSONObject jo = new JSONObject();
        try{
            jo.put("name",nameP);
            jo.put("category",category);
            jo.put("description",description);
            jo.put("address-street",addressStreet);
            jo.put("address-title",addressTitle);
            jo.put("latitude",lat);
            jo.put("longitude",lon);
            jo.put("elevation",ele);
        }catch (Exception ex){
            System.out.println(this.getClass().getSimpleName()+
                    ": error converting to json");
        }
        return jo;
    }




    public String toJsonString(){
        String ret = "";
        try{
            ret = this.toJson().toString();
        }catch (Exception ex){
            System.out.println(this.getClass().getSimpleName()+
                    ": error converting to json string");
        }
        return ret;
    }


}
