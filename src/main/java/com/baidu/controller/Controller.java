package com.baidu.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private Sender sender;

    @RequestMapping("/send")
    @ResponseBody
    public String send(String msg) {
		for (int i = 0; i < 10; i++) {
			Student student = new Student();
			student.setAge(i);
			student.setName("张三");
			sender.send(student,i);
		}

        return "Send OK.";
    }
}
