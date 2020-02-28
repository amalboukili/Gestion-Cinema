package org.sid.cinema.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.sid.cinema.entities.Categorie;
import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Place;
import org.sid.cinema.entities.Projection;
import org.sid.cinema.entities.Salle;
import org.sid.cinema.entities.Seance;
import org.sid.cinema.entities.Ticket;
import org.sid.cinema.entities.Ville;
import org.sid.cinema.repositories.CategorieRepository;
import org.sid.cinema.repositories.CinemaRepository;
import org.sid.cinema.repositories.FilmRepository;
import org.sid.cinema.repositories.PlaceRepository;
import org.sid.cinema.repositories.ProjectionRepository;
import org.sid.cinema.repositories.SalleRepository;
import org.sid.cinema.repositories.SeanceRepository;
import org.sid.cinema.repositories.TicketRepository;
import org.sid.cinema.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional // toutes les methodes sont transactionnelles
public class CinemaInitServiceImpl implements ICinemaInitService {

    @Autowired
    private VilleRepository VilleRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void initVilles() {
        Stream.of("Casablanca", "Marrakech", "Rabat", "Tanger", "Fes").forEach(nameVille -> {
            Ville ville = new Ville();
            ville.setName(nameVille);
            VilleRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {
        VilleRepository.findAll().forEach(ville -> {
            Stream.of("Megarama", "IMAX", "FOUOUN", "CHAHRAZAD", "Astor", "REX").forEach(nameCinema -> {
                Cinema cinema = new Cinema();
                cinema.setName(nameCinema);
                cinema.setNombreSalles(3 + (int) (Math.random() * 7));
                cinema.setVille(ville);
                cinemaRepository.save(cinema);
            });
        });
    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema -> {
            for (int i = 0; i < cinema.getNombreSalles(); i++) {
                Salle salle = new Salle();
                salle.setName("Salle " + (i + 1));
                salle.setCinema(cinema);
                salle.setNombrePlace(15 + (int) (Math.random() * 20));
                salleRepository.save(salle);
            }
        });
    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle -> {
            for (int i = 0; i < salle.getNombrePlace(); i++) {
                Place place = new Place();
                place.setNumero(i + 1);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });
    }

    @Override
    public void initSeance() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Stream.of("12:00", "15:00", "17:00", "19:00", "21:00").forEach(s -> {
            Seance seance = new Seance();
            try {
                seance.setHeureDebut(dateFormat.parse(s));
                seanceRepository.save(seance);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initCategories() {
        Stream.of("Action", "Fantastique", "Drama", "Romantique", "Fiction", "Comedie", "Histoire").forEach(cat -> {
            Categorie categorie = new Categorie();
            categorie.setName(cat);
            categorieRepository.save(categorie);
        });
    }

    @Override
    public void initFilms() {
        double[] durees = new double[] { 1, 1.5, 2, 2.5, 3 };
        List<Categorie> categories = categorieRepository.findAll();
        Stream.of("Game of thrones", "Seigneur des anneaux", "Spider man", "IRON man", "Cat Women")
                .forEach(titreFilm -> {
                    Film film = new Film();
                    film.setTitre(titreFilm);
                    film.setDuree(durees[new Random().nextInt(durees.length)]);
                    film.setPhoto(titreFilm.replaceAll(" ", "").toLowerCase());
                    film.setCategorie(categories.get(new Random().nextInt(categories.size())));
                    filmRepository.save(film);
                });
    }

    @Override
    public void initProjections() {
        double[] prices = new double[] { 30, 50, 60, 70, 80, 90, 100 };
        VilleRepository.findAll().forEach(ville -> {
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle -> {
                    filmRepository.findAll().forEach(film -> {
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection = new Projection();
                            projection.setDateProjection(new Date());
                            projection.setFilm(film);
                            projection.setPrix(prices[new Random().nextInt(prices.length)]);
                            projection.setSalle(salle);
                            projection.setSeance(seance);
                            projectionRepository.save(projection);
                        });
                    });
                });
            });
        });
    }

    @Override
    public void inittickets() {
        projectionRepository.findAll().forEach(proj -> {
            proj.getSalle().getPlaces().forEach(place -> {
                Ticket ticket = new Ticket();
                ticket.setPlace(place);
                ticket.setPrix(proj.getPrix());
                ticket.setProjection(proj);
                ticket.setReserve(false);
                ticketRepository.save(ticket);
            });
        });
    }
}