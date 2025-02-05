package com.g2.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.g2.Components.GenericObjectComponent;
import com.g2.Components.PageBuilder;
import com.g2.Components.UserProfileComponent;
import com.g2.Interfaces.ServiceManager;
import com.g2.Model.User;
import com.g2.Service.AchievementService;


/*
 * Tutte le chiamate legate al profilo utente 
 */
@CrossOrigin
@Controller
public class UserProfileController {

    private final ServiceManager serviceManager;
    @Autowired
    private AchievementService achievementService;

    public UserProfileController(RestTemplate restTemplate) {
        this.serviceManager = new ServiceManager(restTemplate);
    }

    @GetMapping("/profile")
    public String profilePagePersonal(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder profile = new PageBuilder(serviceManager, "profile", model, jwt);
        profile.SetAuth();
        User user = (User) serviceManager.handleRequest("T23", "GetUser", profile.getUserId());
        if (user == null) {
            //Qua gestisco utente sbagliato
            return "error";
        }
        profile.setObjectComponents(
            new UserProfileComponent(serviceManager, user, profile.getUserId(), achievementService, false)
        );
        return profile.handlePageRequest();
    }

    @GetMapping("/Team")
    public String ProfileTeamPage(Model model, @CookieValue(name = "jwt", required = false) String jwt){
        PageBuilder profile = new PageBuilder(serviceManager, "team", model, jwt);
        profile.SetAuth();
        return null;
    }

    @GetMapping("/notification")
    public String ProfileNotificationPage(Model model, @CookieValue(name = "jwt", required = false) String jwt){
        return "notification";
    }

    /*
     *    TENERE QUESTA CHIAMATA SOLO PER DEBUG DA DISATTIVARE 
     * 
     */
    @GetMapping("/profile/{playerID}")
    public String profilePage(Model model,
            @PathVariable(value = "playerID") String playerID,
            @CookieValue(name = "jwt", required = false) String jwt) {

        PageBuilder profile = new PageBuilder(serviceManager, "profile", model);
        profile.SetAuth(jwt);
        User user = (User) serviceManager.handleRequest("T23", "GetUser", playerID);
        if (user == null) {
            //Qua gestisco utente sbagliato
        }
        profile.setObjectComponents(
                new UserProfileComponent(serviceManager, user, playerID, achievementService, false)
        );
        return profile.handlePageRequest();
    }

    @GetMapping("/friend/{playerID}")
    public String friendProfilePage(Model model, @PathVariable(value = "playerID") String playerID, @CookieValue(name = "jwt", required = false) String jwt){
        PageBuilder profile = new PageBuilder(serviceManager, "friend_profile", model, jwt);
        profile.SetAuth();
        User Friend_user = (User) serviceManager.handleRequest("T23", "GetUser", playerID);
        if (Friend_user == null) {
            //Qua gestisco utente sbagliato
            return "error";
        }
        User user = (User) serviceManager.handleRequest("T23", "GetUser", profile.getUserId());
        boolean isFollowing = Friend_user.getFollowersList().contains(user.getUserProfile().getID());
        profile.setObjectComponents(
            new UserProfileComponent(serviceManager, Friend_user, playerID, achievementService, true),
            new GenericObjectComponent("isFollowing", isFollowing),
            new GenericObjectComponent("userId", playerID)
        );
        return profile.handlePageRequest();
    }

    /*
     * Andrebbe gestito che ogni uno può mettere la foto che vuole con i tipi Blob nel DB
     */
    private List<String> getProfilePictures(){
        List<String> list_images = new ArrayList<>();
        list_images.add("default.png");
        list_images.add("men-1.png");
        list_images.add("men-2.png");
        list_images.add("men-3.png");
        list_images.add("men-4.png");
        list_images.add("women-1.png");
        list_images.add("women-2.png");
        list_images.add("women-3.png");
        list_images.add("women-4.png");
        return list_images;
    }


    @GetMapping("/edit_profile")
    public String aut_edit_profile(Model model, @CookieValue(name = "jwt", required = false) String jwt) {        
        PageBuilder Edit_Profile = new PageBuilder(serviceManager, "Edit_Profile", model, jwt);
        Edit_Profile.SetAuth();
        User user = (User) serviceManager.handleRequest("T23", "GetUser", Edit_Profile.getUserId());
        if (user == null) {
            //Qua gestisco utente sbagliato
            return "error";
        }
        // Prendiamo le risorse dal servizio UserProfileService
        List<String> list_images = getProfilePictures();
        Edit_Profile.setObjectComponents(
            new GenericObjectComponent("user", user),
            new GenericObjectComponent("images", list_images)
        );
        return Edit_Profile.handlePageRequest();
    }

}
