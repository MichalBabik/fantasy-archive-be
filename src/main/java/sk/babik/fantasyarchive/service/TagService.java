package sk.babik.fantasyarchive.service;

import sk.babik.fantasyarchive.persistence.model.FantasyUser;
import sk.babik.fantasyarchive.persistence.model.Tag;

import java.util.List;

public interface TagService {

    Tag createTag(String tagDto);

    boolean isTagAvailable(String name);

    Tag getTag(Long id);

    boolean deleteTagById(Long id);

    List<Tag> getAllTags();

}
