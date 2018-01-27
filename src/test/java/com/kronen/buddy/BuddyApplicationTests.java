package com.kronen.buddy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kronen.buddy.web.i18n.I18NService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuddyApplicationTests {

    @Autowired
    private I18NService i18NService;
    
    @Test
    public void testMessageByLocaleService() {
	String expected = "Buddy App";
	String messageId = "index.main.callout";
	String actual = i18NService.getMessage(messageId);
	assertEquals("The actual and expected String don't match", expected, actual);
    }

}
