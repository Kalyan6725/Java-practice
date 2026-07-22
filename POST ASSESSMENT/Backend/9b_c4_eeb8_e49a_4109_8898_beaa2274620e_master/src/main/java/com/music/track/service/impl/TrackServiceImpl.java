package com.music.track.service.impl;

import com.music.track.dto.TrackRequest;
import com.music.track.model.Track;
import com.music.track.repository.TrackRepository;
import com.music.track.service.TrackService;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TrackServiceImpl implements TrackService {
    private final TrackRepository trackRepository;

    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public Track createTrack(TrackRequest trackRequest) {
        Track newTrack = new Track();
        String newTitle=trackRequest.title().toUpperCase();
        newTrack.setTitle(newTitle);
        newTrack.setAlbumName(trackRequest.albumName());
        newTrack.setReleaseDate(trackRequest.releaseDate());
        newTrack.setPlayCount(trackRequest.playCount());
        return trackRepository.save(newTrack);
    }

    @Override
    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    @Override
    public void deleteTrack(Long trackId) {
        trackRepository.deleteById(trackId);
    }

    @Override
    public Track getTracksByTitle(String title)  {
        String titleUpperCase = title.toUpperCase();
        return trackRepository.findByTitle(titleUpperCase);
    }

    @Override
    public List<Track> getTracksByAlbum(String album) {
        return trackRepository.findByAlbumName(album);
    }

}
