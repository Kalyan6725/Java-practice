package com.music.track.controller;

import com.music.track.dto.TrackRequest;
import com.music.track.model.Track;
import com.music.track.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("music/platform/v1/tracks")
@CrossOrigin(origins = "http://localhost:4200")
public class TrackController {
    private final TrackService trackService;
    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }
    
    @PostMapping()
    public ResponseEntity<Track> createTrack(@RequestBody TrackRequest trackRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(trackService.createTrack(trackRequest));
    }

    @GetMapping()
    public ResponseEntity<List<Track>> getAllTracks(){
        return ResponseEntity.ok(trackService.getAllTracks());
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long trackId){
        trackService.deleteTrack(trackId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Track> getTrackByTitle(@RequestParam String title) throws ParseException {
        return ResponseEntity.ok(trackService.getTracksByTitle(title));
    }

    @GetMapping("/searchByAlbum")
    public ResponseEntity<List<Track>> getTrackByAlbum(@RequestParam String album) throws ParseException {
        return ResponseEntity.ok(trackService.getTracksByAlbum(album));
    }

}
