package org.sid.cinema.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.sid.cinema.entities.Film;
import org.sid.cinema.repositories.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaRestController {
    @Autowired
    private FilmRepository filmRepository;

    @GetMapping("/listFilms")
    public List<Film> films() {
        return filmRepository.findAll();
    }

    // comment consulter une image
    @GetMapping(path = "/imageFilm/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable(name = "id") Long id) throws IOException {
        Film f = filmRepository.findById(id).get();
        String photoName = f.getPhoto();
        File file = new File(System.getProperty("user.home") + "/cinema/images/" + photoName + ".jpg");
        Path path = Paths.get(file.toURI());
        return Files.readAllBytes(path);

    }

}