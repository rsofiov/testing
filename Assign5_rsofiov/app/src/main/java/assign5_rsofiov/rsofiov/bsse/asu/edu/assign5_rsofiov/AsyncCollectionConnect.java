package assign5_rsofiov.rsofiov.bsse.asu.edu.assign5_rsofiov;

import android.os.AsyncTask;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by emilybyrne on 3/21/17.
 */


public class AsyncCollectionConnect extends AsyncTask<MethodInformation, Integer, MethodInformation> {

    public String url = "http://10.0.2.2:8081/";
    @Override
    protected void onPreExecute(){
        android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest){
        // array of methods to be called. Assume exactly one in a single MethodInformation object
        Place aStud = new Place();//Place aStud = new Place("unknown",0,(new String[]{"unknown"}));
        android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            JSONArray ja = new JSONArray(aRequest[0].params);
            android.util.Log.d(this.getClass().getSimpleName(),"params: "+ja.toString());
            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";
            android.util.Log.d(this.getClass().getSimpleName(),"requestData: "+requestData+" url: "+aRequest[0].urlString);
            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)), aRequest[0].parent);
            String resultStr = conn.call(requestData);
            aRequest[0].resultAsJson = resultStr;
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation res){
        android.util.Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        android.util.Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);
        try {
            if (res.method.equals("getNames")) {
                JSONObject jo = new JSONObject(res.resultAsJson);
                JSONArray ja = jo.getJSONArray("result");
                ArrayList<String> al = new ArrayList<String>();
                for (int i = 0; i < ja.length(); i++) {
                    al.add(ja.getString(i));
                }
                String[] names = al.toArray(new String[0]);
                res.parent.adapter.clear();
                for (int i = 0; i < names.length; i++) {
                    res.parent.adapter.add(names[i]);
                }
                res.parent.adapter.notifyDataSetChanged();
                if (names.length > 0){
                    try{
                        MethodInformation mi = new MethodInformation(res.parent, res.urlString, "get",
                                new String[]{names[0]});
                        AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
                    } catch (Exception ex){
                        android.util.Log.w(this.getClass().getSimpleName(),"Exception processing spinner selection: "+
                                ex.getMessage());
                    }
                }
            } else if (res.method.equals("get")) {
                JSONObject jo = new JSONObject(res.resultAsJson);
                Place aStud = new Place(jo.getJSONObject("result"));
                res.parent.nameP.setText(aStud.nameP);
                res.parent.category.setText(aStud.category);
                res.parent.description.setText(aStud.description);
                res.parent.addressStreet.setText(aStud.addressStreet);
                res.parent.addressTitle.setText(aStud.addressTitle);
                res.parent.lon.setText((new Double(aStud.lon)).toString());
                res.parent.lat.setText((new Double(aStud.lat)).toString());
                res.parent.ele.setText((new Double(aStud.ele)).toString());
            }else if(res.method.equals("add")){
                try {
                    MethodInformation mi = new MethodInformation(res.parent, res.urlString, "getNames",
                            new String[]{});
                    AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
                } catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                            ex.getMessage());
                }
            }
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"Exception: "+ex.getMessage());
        }
    }


}
