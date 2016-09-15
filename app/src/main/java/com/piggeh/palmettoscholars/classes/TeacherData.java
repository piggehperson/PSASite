package com.piggeh.palmettoscholars.classes;

public class TeacherData {
    private String name;
    private String subject;
    private int category;
    private int prefix;
    private String email;
    private String bio;
    private String identifier;

    public TeacherData(){}

    public void setName(String name){this.name = name;}
    public String getName(){return name;}

    public void setSubject(String subject){this.subject = subject;}
    public String getSubject(){return subject;}

    public void setCategory(int category){this.category = category;}
    public int getCategory(){return category;}

    public void setPrefix(int prefix){this.prefix = prefix;}
    public int getPrefix(){return prefix;}

    public void setEmail(String email){this.email = email;}
    public String getEmail(){return email;}

    public void setBio(String bio){this.bio = bio;}
    public String getBio(){return bio;}

    public void setIdentifier(String identifier){this.identifier = identifier;}
    public String getIdentifier(){return identifier;}
}
