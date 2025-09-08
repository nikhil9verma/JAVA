package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;






    @PostMapping
    public ResponseEntity<JournalEntry>  createEntry(@RequestBody JournalEntry myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            journalEntryService.saveEntry(myEntry,userName);

            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);

        }
        catch (Exception e) {
            return new ResponseEntity<>(myEntry, HttpStatus.BAD_REQUEST);

        }


    }

    @Autowired
    private UserService userService;

//    @GetMapping
//    public ResponseEntity<?> getAllJournalEntriesOfUser(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//        System.out.println("Looking for user: " + userName); // Debug
//
//        User user = userService.findUserByUsername(userName);
//        System.out.println("Found user: " + (user != null)); // Debug
//
//        if (user == null) {
//            return new ResponseEntity<>("User not found", HttpStatus.NO_CONTENT);
//        }
//
//        List<JournalEntry> all = user.getJournalEntries();
//        System.out.println("Journal entries count: " + all.size()); // Debug
//        if(all.size()==0){
//            return  new ResponseEntity<>("No journal entries found", HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(all, HttpStatus.OK);
//    }


    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user=userService.findUserByUsername(userName);
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        List<JournalEntry> collect= user.getJournalEntries().stream().filter(x-> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry1 = journalEntryService.findById(myId);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<JournalEntry>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<JournalEntry>( HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        boolean removed=journalEntryService.deleteById(myId,userName);
        if(removed){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new  ResponseEntity<>(HttpStatus.NO_CONTENT) ;
    }

    @PutMapping ("/id/{myid}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myid , @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user=userService.findUserByUsername(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x-> x.getId().equals(myid)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myid);
            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();
                old.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("")?newEntry.getTitle():old.getTitle());
                old.setContent(newEntry.getContent()!=null && !newEntry.getContent().equals("") ? newEntry.getContent() : "");
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old,HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

