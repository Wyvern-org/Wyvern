package wyvern.util.jobs;

import com.google.gson.JsonObject;
import wyvern.util.Util;

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
                return Util.httpPOST("https://wyvernapp.com/api/v1/authenticate", req.getAsString());
            } catch (IOException ignored) {
                //TODO: something went oops. handle it here
                return null;
            }
        }, postJob);
    }
}
