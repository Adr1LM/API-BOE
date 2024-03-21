package com.paellasoft.CRUD.chatGpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class Message {

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    private String role;
    private String content; //prompt



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
