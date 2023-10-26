package wyvern.util.jobs;

import com.google.gson.JsonObject;
import wyvern.util.Util;
import wyvern.util.http.HttpRequest;

import java.io.IOException;

public class AuthJob extends AsyncJob
{
    /**
     * Creates a new job for authenticating the user with the provided credentials.
     * @param id It's called "id" for when we eventually allow both email & username logins.
     * @param password Password.
     * @param postJob The code to run post-job, with job result included.
     */
    public AuthJob(String id, String password, IPostJob postJob)
    {
        super(() ->
        {
            try
            {
                JsonObject req = new JsonObject();
                req.addProperty("username", id);
                req.addProperty("password", password);

                return new HttpRequest("http://prod.uas.wyvernapp.com/v1/authenticate")
                .setRequestMethod("POST")
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
