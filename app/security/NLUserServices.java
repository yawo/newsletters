package security;

import models.Activations;
import models.Users;
import play.Logger;
import play.libs.Codec;
import securesocial.provider.SocialUser;
import securesocial.provider.UserId;
import securesocial.provider.UserService;

/**
 * Created by IntelliJ IDEA.
 * User: yawo
 * Date: 10/12/11
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 */
public class NLUserServices implements UserService.Service {

    //private Map<String, SocialUser> users = Collections.synchronizedMap(new HashMap<String, SocialUser>());
    //private Map<String, SocialUser> activations = Collections.synchronizedMap(new HashMap<String, SocialUser>());

    public SocialUser find(UserId id) {
        SocialUser u = null;
        try {
            Users us = Users.find("byUserKey",id.id + id.provider.toString()).first();
            u=us.socialUser;
            //Logger.info("Found user:"+ );
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return u;
    }

    public void save(SocialUser user) {
        Users u = null;
        user.serviceInfo=null;
        Logger.info("saving "+user.displayName);
        try {
           u = Users.find("byUserKey",user.id.id + user.id.provider.toString()).first();
           u.socialUser=user;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            u = new Users(user.id.id + user.id.provider.toString(),user);
        }

        u.save();
    }

    public String createActivation(SocialUser user) {
        final String uuid = Codec.UUID();
        Activations a = new Activations(uuid,user);
        a.save();
        return uuid;
    }

    public boolean activate(String uuid) {
        Logger.info("UUID to look:"+uuid);
        Activations u = Activations.find("byUserKey",uuid).first();
        SocialUser user = u.socialUser;
        boolean result = false;

        if( user != null ) {
            user.isEmailVerified =  true;
            save(user);
            Logger.info(user+" saved");
            u.delete();
            result = true;
        }
        return result;
    }

    public void deletePendingActivations() {
        Activations.deleteAll();
    }
}

