package security;

import models.Activations;
import models.Users;
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
        Users u = Users.find("byUserKey",id.id + id.provider.toString()).first();
        return u.socialUser;
    }

    public void save(SocialUser user) {
        Users u = new Users(user.id.id + user.id.provider.toString(),user);
        u.save();
    }

    public String createActivation(SocialUser user) {
        final String uuid = Codec.UUID();
        Activations a = new Activations(uuid,user);
        a.save();
        return uuid;
    }

    public boolean activate(String uuid) {
        Users u = Activations.find("byUserKey",uuid).first();
        SocialUser user = u.socialUser;
        boolean result = false;

        if( user != null ) {
            user.isEmailVerified =  true;
            save(user);
            u.delete();
            result = true;
        }
        return result;
    }

    public void deletePendingActivations() {
        Activations.deleteAll();
    }
}

