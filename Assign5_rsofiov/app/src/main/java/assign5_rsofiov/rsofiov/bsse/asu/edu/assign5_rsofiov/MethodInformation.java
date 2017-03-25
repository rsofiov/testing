package assign5_rsofiov.rsofiov.bsse.asu.edu.assign5_rsofiov;

/**
 * Created by emilybyrne on 3/21/17.
 */

public class MethodInformation {
    public String method;
    public Object[] params;
    public MainActivity parent;
    public String urlString;
    public String resultAsJson;

    MethodInformation(MainActivity parent, String urlString, String method, Object[] params){
        this.method = method;
        this.parent = parent;
        this.urlString = urlString;
        this.params = params;
        this.resultAsJson = "{}";
    }


}
