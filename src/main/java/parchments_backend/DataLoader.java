package parchments_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import parchments_backend.domain.Parchment;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.ParchmentRepository;
import parchments_backend.repositories.WriterRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {
    private Writer firstWriter;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private ParchmentRepository parchmentRepository;

    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void run(ApplicationArguments args) {
        seedWritersTable();
        seedParchmentsTable();
    }

    private void seedWritersTable() {
        List<Writer> presentWriters = writerRepository.findAll();
        if (presentWriters.size() >= 1) {
            return;
        }

        firstWriter = new Writer("Writer", getEncoder().encode("1"));

        writerRepository.save(firstWriter);
    }

    private void seedParchmentsTable() {
        List<Parchment> presentParchments = parchmentRepository.findAll();
        if (presentParchments.size() >= 3) {
            return;
        }

        String theDoorText = "A door stands in front of our hero, waiting to be opened. They could turn away and leave, but it has a gilded doorknob and it looks like it could be pried off if our hero yanked hard enough. The wood is pretty nice too. It could be used to make furniture.";
        Parchment theDoor = new Parchment("The Door", theDoorText, theDoorText);
        String theWindowText = "It looks like there's a window that our hero could break through. It's pitch black on the other side, no idea what's behind.";
        Parchment theWindow = new Parchment("The Window", theWindowText, theWindowText);
        String theSecretTunnelText = "Our hero spots a tiny hole on the left wall. They could crawl through it and see what's on the other side. It could be dangerous though.";
        Parchment theSecretTunnel = new Parchment("The Secret Tunnel", theSecretTunnelText, theSecretTunnelText);
        String theTrapDoorText = "There's a tiny trap door in the middle of the hallway. It doesn't look like it's been opened ever. It'd be wise to leave it like that";
        Parchment theTrapDoor = new Parchment("The Trap Door", theTrapDoorText, theTrapDoorText);
        String theHallwayText = "Our hero could always turn back to where they came from.";
        Parchment theHallway = new Parchment("The Hallway", theHallwayText, theHallwayText);
        String theCeilingText = "It's a ceiling. No way it could open up a path, unless you brought the right tools";
        Parchment theCeiling = new Parchment("The Ceiling", theCeilingText, theCeilingText);
        String theWasteOfTimeText = "Our hero wanders around from one end of the hallway to the other, not quite doing anything.";
        Parchment theWasteOfTime = new Parchment("The Waste of Time", theWasteOfTimeText, theWasteOfTimeText);
        String thePlagueText = "Unfortunately, a terrible poisonous cloud was waiting for our hero behind the door. He was never seen again.";
        Parchment thePlague = new Parchment("The Plague", thePlagueText, thePlagueText);
        String theAncientScrollText = "An only piece of paper lied in the center of the room. Its ancient wisdom prayed:\n" +
                "\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi eu erat laoreet, cursus libero ut, commodo neque. Aliquam pellentesque vulputate dignissim. Aliquam volutpat, ante at facilisis pellentesque, ipsum massa venenatis urna, sit amet vehicula lacus arcu sit amet ligula. Aliquam vestibulum elit augue, bibendum tempus nibh viverra ut. Donec mi turpis, egestas vitae molestie a, vestibulum at nulla. Curabitur sed tortor enim. Aenean dapibus sapien est. Praesent eu diam sit amet enim lobortis sollicitudin. Quisque eu ex id urna laoreet eleifend et vulputate tellus. Nulla rhoncus mauris at tempor ornare. Proin tincidunt, diam tristique auctor tempor, lacus diam mattis augue, ut tempus felis turpis a nisl. Praesent ornare ornare elit sit amet ultricies. Nam egestas tortor mi, et suscipit diam varius consectetur. Phasellus nec eros porttitor, viverra dolor eget, condimentum turpis. Quisque nec metus tincidunt, hendrerit ex eget, pretium mauris.";
        Parchment theAncientScroll = new Parchment(
                "The Ancient Scroll",
                theAncientScrollText,
                theAncientScrollText
        );
        Parchment theOtherDoor = generateTheOtherDoorParchments();

        theDoor.setContinuations(Arrays.asList(thePlague, theOtherDoor, theAncientScroll));
        firstWriter.setParchments(Arrays.asList(theDoor, thePlague, theAncientScroll, theWindow, theSecretTunnel, theTrapDoor, theHallway, theCeiling, theWasteOfTime));
        writerRepository.save(firstWriter);
    }

    private Parchment generateTheOtherDoorParchments() {
        String theOtherDoorText = "To their surprise, another door waited behind the last one.";
        Parchment theOtherDoor = new Parchment(firstWriter, "The Other Door", theOtherDoorText, theOtherDoorText);

        Parchment currentDoor = theOtherDoor;
        for (int i = 0; i < 10; i++) {
            Parchment newDoor = new Parchment(firstWriter, theOtherDoor.getTitle(), theOtherDoor.getSynopsis(), theOtherDoor.getContents());
            currentDoor.setContinuations(Collections.singletonList(newDoor));
            currentDoor = newDoor;
        }
        return theOtherDoor;
    }
}
