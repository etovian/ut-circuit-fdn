package com.utcfdn.congregation;

import com.utcfdn.person.PersonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CongregationMapperTest {

    private CongregationMapper congregationMapper;

    @Mock
    private ResourcePatternResolver resourcePatternResolver;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private CongregationExternalLinkMapper congregationExternalLinkMapper;

    @BeforeEach
    void setUp() {
        congregationMapper = new CongregationMapper(resourcePatternResolver, personMapper, congregationExternalLinkMapper);
    }

    @Test
    void testToDto_NullEntity() {
        assertNull(congregationMapper.toDto((CongregationEntity) null));
    }

    @Test
    void testToDto_BasicMapping() throws IOException {
        CongregationEntity entity = CongregationEntity.builder()
                .id(1L)
                .name("Test Church")
                .description("Desc")
                .mission("Mission")
                .bannerPhotoDirectory("test-dir")
                .build();
        
        entity.setAddresses(Collections.emptyList());
        entity.setPersons(Collections.emptyList());
        entity.setExternalLinks(Collections.emptyList());

        // Mock resources for banner photos
        Resource resource1 = mock(Resource.class);
        when(resource1.isReadable()).thenReturn(true);
        when(resource1.getFilename()).thenReturn("photo1.jpg");

        Resource resource2 = mock(Resource.class);
        when(resource2.isReadable()).thenReturn(true);
        when(resource2.getFilename()).thenReturn("photo2.png");

        when(resourcePatternResolver.getResources(anyString())).thenReturn(new Resource[]{resource1, resource2});

        CongregationDto dto = congregationMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Church", dto.getName());
        assertEquals(2, dto.getBannerPhotos().size());
        assertTrue(dto.getBannerPhotos().contains("/api/banner-photos/test-dir/photo1.jpg"));
        assertTrue(dto.getBannerPhotos().contains("/api/banner-photos/test-dir/photo2.png"));
    }

    @Test
    void testToDto_ResourceResolverException() throws IOException {
        CongregationEntity entity = CongregationEntity.builder()
                .id(1L)
                .name("Test Church")
                .bannerPhotoDirectory("error-dir")
                .build();
        
        entity.setAddresses(Collections.emptyList());
        entity.setPersons(Collections.emptyList());
        entity.setExternalLinks(Collections.emptyList());

        when(resourcePatternResolver.getResources(anyString())).thenThrow(new IOException("Test Error"));

        CongregationDto dto = congregationMapper.toDto(entity);

        assertNotNull(dto);
        assertTrue(dto.getBannerPhotos().isEmpty(), "Banner photos should be empty if IOException occurs");
    }
}
