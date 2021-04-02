package com.ruhul.admin_app.model;

public  class StoryModel {


    //curenttime,child_story_name_node,story,partnumber


    private String curenttime;
    private String child_story_name_node;
    private String story;
    private String partnumber;
    private String imageuri;

    public String getImageuri() {
        return imageuri;
    }

    public String getCurenttime() {
        return curenttime;
    }

    public String getChild_story_name_node() {
        return child_story_name_node;
    }

    public String getStory() {
        return story;
    }

    public String getPartnumber() {
        return partnumber;
    }



    public StoryModel() {
    }



    public StoryModel(String curenttime, String child_story_name_node, String story, String partnumber,String imageuri) {
        this.curenttime = curenttime;
        this.child_story_name_node = child_story_name_node;
        this.story = story;
        this.partnumber = partnumber;
        this.imageuri=imageuri;
    }







}
