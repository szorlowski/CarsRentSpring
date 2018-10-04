package com.risalyth.connection;

import com.risalyth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

public class ConnectionTest {

    @Autowired
    UserService userService;

    @Test
    public void testConnection(){
        Assert.notNull(userService.getUsers());
    }
}
