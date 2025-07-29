package walid.jahin.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import walid.jahin.model.ArtistSection;
import walid.jahin.repository.ArtistSectionRepository;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ArtistSectionRepository repository;

    public DataInitializer(ArtistSectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        List<String> keys = List.of(
            "major_solo",
            "international",
            "solo",
            "group",
            "achievement",
            "collections",
            "work_style"
        );

        for (String key : keys) {
            repository.findBySectionKey(key).orElseGet(() ->
                repository.save(new ArtistSection(key, ""))
            );
        }
    }
}
