package wyvern.util.jobs;

import com.google.gson.JsonObject;
import wyvern.util.Util;
import wyvern.util.http.HttpRequest;

public class RegisterJob extends AsyncJob
{
    public RegisterJob(String email, String username, String password, IPostJob postJob)
    {
        super(() ->
        {
            try
            {
                JsonObject req = new JsonObject();
                req.addProperty("email", email);
                req.addProperty("username", username);
                req.addProperty("password", password);

                return new HttpRequest("http://prod.uas.wyvernapp.com/v1/user")
                .setRequestMethod("PUT")
                .setContentType("application/json")
                .writeString(req.toString())
                .readResponse();
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                return null;
            }
        }, postJob);
    }
}
