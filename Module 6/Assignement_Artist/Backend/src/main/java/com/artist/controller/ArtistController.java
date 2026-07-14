package com.artist.controller;

import com.artist.dto.ArtistRequest;
import com.artist.model.Artist;
import com.artist.service.ArtistService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/artists")
@CrossOrigin("*")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @PostMapping
    public ResponseEntity<Artist> createPlayList(
            @RequestBody ArtistRequest artistRequest) {

        Artist saved = artistService.createArtist(artistRequest);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<Artist> getArtistById(
            @PathVariable Long artistId) {

        Artist artist = artistService.getArtistByID(artistId);

        if (artist == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(artist);
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() {

        return ResponseEntity.ok(
                artistService.getArtists()
        );
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> deleteArtist(
            @PathVariable Long artistId) {

        Artist artist = artistService.getArtistByID(artistId);

        if (artist == null) {
            return ResponseEntity.notFound().build();
        }

        artistService.deleteArtist(artistId);

        return ResponseEntity.noContent().build();
    }
}