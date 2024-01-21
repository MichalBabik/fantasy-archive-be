package sk.babik.fantasyarchive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.babik.fantasyarchive.persistence.model.FantasyUser;
import sk.babik.fantasyarchive.persistence.model.Tag;

import sk.babik.fantasyarchive.persistence.repository.TagRepository;

import java.util.List;


@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag createTag(String name) {
        Tag newTag = new Tag();
        newTag.setName(name);
        System.out.println("Tag " + name + " successfully created");
        return tagRepository.save(newTag);
    }

    @Override
    public Tag getTag(Long id) {
        boolean isExistingTag = tagRepository.existsById(id);
        if (isExistingTag) {
            System.out.println("Tag successfully returned!");
            return tagRepository.findById(id).get();
        } else {
            System.out.println("Tag with id: " + id + ", doesn't exist!");
            return null;
        }
    }


    @Override
    public boolean isTagAvailable(String name) {
        String lowercaseName = name.toLowerCase();
        for (Tag tag : getAllTags()) {
            if (tag.getName().toLowerCase().equals(lowercaseName)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean deleteTagById(Long id) {
        boolean isExistingTag = tagRepository.existsById(id);
        if (isExistingTag) {
            tagRepository.deleteById(id);
            System.out.println("Tag successfully deleted!");
            return true;
        } else {
            System.out.println("Cannot delete because tag with id: " + id + ", doesn't exist!");
            return false;
        }
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}
