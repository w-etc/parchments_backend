package parchments_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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


    public void run(ApplicationArguments args) {
        seedWritersTable();
        seedParchmentsTable();
    }

    private void seedWritersTable() {
        List<Writer> presentWriters = writerRepository.findAll();
        if (presentWriters.size() >= 1) {
            return;
        }

        firstWriter = new Writer("Escritor", "");

        writerRepository.save(firstWriter);
    }

    private void seedParchmentsTable() {
        List<Parchment> presentParchments = parchmentRepository.findAll();
        if (presentParchments.size() >= 3) {
            return;
        }

        Parchment theDoor = generateTheDoorParchment();
        Parchment thePlague = generateThePlagueParchment();
        Parchment theAncientScroll = generateLoremIpsumParchment();
        Parchment theOtherDoor = generateTheOtherDoorParchments();

        theDoor.setContinuations(Arrays.asList(thePlague, theOtherDoor, theAncientScroll));
        firstWriter.setParchments(Arrays.asList(theDoor, thePlague, theAncientScroll));
        writerRepository.save(firstWriter);
    }

    private Parchment generateTheDoorParchment() {
        return new Parchment("La Puerta", "Quedan pocas cosas por quemar en el último piso del calabozo. No hay ni un trozo de madera en el pasillo de piedra que recorre nuestro protagonista, ni carne en los esqueletos que le hacen compañía. Alimentar el fuego de su antorcha con los harapos que llevan estos cuerpos solo sería una opción si fuera lo suficientemente valiente para siquiera mirarlos. Si están ahí es porque algo los mató. Reconocer su presencia no es una opción.\n" +
                "\n" +
                "Podía darse la vuelta y marcharse. Eventualmente volvería al Sol que dejó detrás, si es que todavía era de día. Lograría volver, dejando todos los horrores detrás, con las manos vacías. Sin haber revisado la última puerta enfrente suyo.\n" +
                "\n" +
                "Había llegado tan lejos por una razón. El último título de la tecnicatura se encontraba en alguna parte del calabozo. Tal vez estaba a punto de encontrarlo.\n" +
                "\n" +
                "Nuestro protagonista quitó la mano esquelética del picaporte y lo giró.");
    }

    private Parchment generateThePlagueParchment() {
        return new Parchment("La Plaga", "Lamentablemente una terrible nube venenosa estaba esperando a nuestro protagonista detrás de la puerta. Nunca se lo volvió a ver.");
    }

    private Parchment generateLoremIpsumParchment() {
        return new Parchment("El Pergamino Antiguo", "Un único pedazo de papel yacía en el centro del pequeño cuarto detrás de la puerta. Su antigua sabiduría rezaba:\n" +
                "\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi eu erat laoreet, cursus libero ut, commodo neque. Aliquam pellentesque vulputate dignissim. Aliquam volutpat, ante at facilisis pellentesque, ipsum massa venenatis urna, sit amet vehicula lacus arcu sit amet ligula. Aliquam vestibulum elit augue, bibendum tempus nibh viverra ut. Donec mi turpis, egestas vitae molestie a, vestibulum at nulla. Curabitur sed tortor enim. Aenean dapibus sapien est. Praesent eu diam sit amet enim lobortis sollicitudin. Quisque eu ex id urna laoreet eleifend et vulputate tellus. Nulla rhoncus mauris at tempor ornare. Proin tincidunt, diam tristique auctor tempor, lacus diam mattis augue, ut tempus felis turpis a nisl. Praesent ornare ornare elit sit amet ultricies. Nam egestas tortor mi, et suscipit diam varius consectetur. Phasellus nec eros porttitor, viverra dolor eget, condimentum turpis. Quisque nec metus tincidunt, hendrerit ex eget, pretium mauris.");
    }

    private Parchment generateTheOtherDoorParchments() {
        Parchment theOtherDoor = new Parchment("La Otra Puerta", "Para su sorpresa, otra puerta lo esperaba detrás de la anterior.");

        Parchment currentDoor = theOtherDoor;
        for (int i = 0; i < 10; i++) {
            Parchment newDoor = new Parchment(theOtherDoor.getTitle(), theOtherDoor.getContents());
            currentDoor.setContinuations(Collections.singletonList(newDoor));
            currentDoor = newDoor;
        }
        return theOtherDoor;
    }
}
