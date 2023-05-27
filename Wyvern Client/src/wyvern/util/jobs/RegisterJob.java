package wyvern.util.jobs;

import com.google.gson.JsonObject;
import wyvern.util.Util;

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
                return Util.httpPOST("https://prod.uas.wyvernapp.com/api/v1/user", req.getAsString());
            } catch (Exception ignored) {
                //TODO: something went oops, handle it here
                return null;
            }
        }, postJob);
    }
}
