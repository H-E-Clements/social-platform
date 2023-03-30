package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.Event;
import com.example.reversiblecomputation.repository.EventRepository;
import com.example.reversiblecomputation.repository.PostRepository;
import com.example.reversiblecomputation.service.EventDateSortService;
import com.example.reversiblecomputation.service.SearchAndIdentifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class EventController {

    //EventController handles requests relating to Events

    //importing the relevant repositories to handle Events
    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;
    @Autowired
    private EventRepository eventRepo;

    // Handles GET requests at '/newEvent', when /newEvent is accessed, this request mapping returns the Event creation form
    @GetMapping("/newEvent")
    public String newEvent(Model model, Authentication authentication){
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);boolean navImg = searchAndIdentifyService.checkImg(authentication);model.addAttribute("navImg", navImg);model.addAttribute("navUser", navUser);try {model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");} catch(Exception e) {}
        //imports navbar image - checks if user is logged in^
        return "events/create";
        //returns page to create a new event
    }

    //Handles POST requests at '/createEvent', after a user submits a request to create an event, this request mapping handles the submission
    @PostMapping("/createEvent")
    public String createEvent(@RequestParam("title") String title,
                              @RequestParam("description") String description,
                              @RequestParam("date") String date,
                              @RequestParam("time") String time,
                              @RequestParam("duration") String duration,
                              @RequestParam("location") String location,
                              Authentication authentication) throws ParseException {
        if(title.isEmpty()){return "redirect:/newEvent?titleErr";}
        if(description.isEmpty()){return "redirect:/newEvent?descriptionErr";}
        if(date.isEmpty()){return "redirect:/newEvent?emptyDateErr";}
        if(time.isEmpty()){return "redirect:/newEvent?emptyTimeErr";}
        if(duration.isEmpty()){return "redirect:/newEvent?emptyDurationErr";}
        if(location.isEmpty()){return "redirect:/newEvent?emptyLocationErr";}
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");LocalDate dateTime = LocalDate.parse(date,formatter);if (dateTime.isAfter(LocalDate.now()) == false){return "redirect:/newEvent?dateErr";}
        //Validates all the inputs, makes sure not empty, also checks if inputted date is in future (if not, returns an error)

        date  = date.split(" ")[0];
        //truncate time off date input, so we dont have duplicate times

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setAuthor(searchAndIdentifyService.userObject(authentication).getName());
        event.setDate(date);
        event.setTime(time);
        event.setDuration(duration);
        event.setLocation(location);
        eventRepo.save(event);
        //creates an event, assigns the inputted values to the event, and stores the event in DB

        return "redirect:/events";
        //redirects user back to the events page
    }

    // Handles GET requests at '/events', when /events is accessed, this request mapping returns the Events page
    @GetMapping("/events")
    public String events(Model model, Authentication authentication, String keyword, String searchType){
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);boolean navImg = searchAndIdentifyService.checkImg(authentication);model.addAttribute("navImg", navImg);model.addAttribute("navUser", navUser);try {model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");} catch(Exception e) {}
        //imports navbar image - checks if user is logged in^

        List<Event> events = null;
        //Creates a List of Events to display (not set yet)
        if (keyword != null){
            if (searchType.equals("title")){events = eventRepo.findByKeywordDescription(keyword);}
            if (searchType.equals("description")){events = eventRepo.findByKeywordDescription(keyword);}
            if (searchType.equals("date")){events = eventRepo.findByKeywordDate(keyword);}
            if (searchType.equals("author")){events = eventRepo.findByKeywordAuthor(keyword);}
            if (searchType.equals("location")){events = eventRepo.findByKeywordLocation(keyword);}
            model.addAttribute("events", events);
        }//Events search feature^ - based on search, sets the list of events

        else {
            events = eventRepo.findAll();
            model.addAttribute("events", events);
        }
        //If user doesn't search (just wants to access normal page), returns a list of all events

        Collections.sort(events, (new EventDateSortService()));
        //Sorts the events in chronological order

        return "events/view";
        //returns page to create a new event
    }

}
