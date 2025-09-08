package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.Repository.JournalEntryRepo;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
public class JournalEntryService {


    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry,String userName) {

        try {
            User user = userService.findUserByUsername(userName);

            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            user.getJournalEntries().add(saved);

            userService.saveUser(user);
        }
        catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("AN error occured while saving entry: ",e);
        }
    }
    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
       return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId Id,String userName) {
        boolean removed = false;
        try {
            User user = userService.findUserByUsername(userName);
             removed = user.getJournalEntries().removeIf(x -> x.getId().equals(Id));
            if (removed) {


                userService.saveUser(user);
                journalEntryRepo.deleteById(Id);

            }
        } catch (Exception e) {
//            logger.info("hhahahahahahahahhaahahahaha");
            throw new RuntimeException("AN error occured while deleting entry: ",e);
        }
        return removed;
    }



}



















