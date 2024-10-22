package unip.universityInParty.domain.course.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unip.universityInParty.domain.course.dto.CourseDto;
import unip.universityInParty.domain.course.entity.Course;
import unip.universityInParty.domain.course.repository.CourseRepository;
import unip.universityInParty.domain.party.entity.Party;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public void create(List<CourseDto> courseDtoList, Party party) {
        saveCourses(courseDtoList, party);
    }

    @Transactional
    public void delete(Long partyId) {
        courseRepository.deleteByPartyId(partyId);
    }

    @Transactional
    public void update(List<CourseDto> courseDtoList, Party party) {
        delete(party.getId());
        saveCourses(courseDtoList, party);
    }

    private void saveCourses(List<CourseDto> courseDtoList, Party party) {
        List<Course> courses = courseDtoList.stream()
            .map(courseDto -> Course.builder()
                .address(courseDto.address())
                .name(courseDto.name())
                .content(courseDto.content())
                .party(party)
                .build())
            .collect(Collectors.toList());
        courseRepository.saveAll(courses);
    }
}