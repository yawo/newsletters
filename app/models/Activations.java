package models;

import play.modules.mongo.MongoEntity;
import play.modules.mongo.MongoModel;
import securesocial.provider.SocialUser;

/**
 * Created by IntelliJ IDEA.
 * User: yawo
 * Date: 09/12/11
 * Time: 01:53
 * To change this template use File | Settings | File Templates.
 */
@MongoEntity
public class Activations extends MongoModel{

    public Activations(String userKey, SocialUser socialUser) {
        this.userKey = userKey;
        this.socialUser = socialUser;
    }

    public String userKey;
        public SocialUser socialUser;
}
