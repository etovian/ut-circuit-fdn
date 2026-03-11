# DTO (COMPLETED)

- Create a PersonDto that maps to PersonEntity and also includes a collection of congregations related to that person. (DONE)
- Update the CongregationDto to include a collection of persons related to that congregation. (DONE)
- Be sure these are mapped via dedicated mapping classes when returned from any controller.

# Repository (COMPLETED)
 
- Update PersonRepository
  - Add a method to return all persons ordered by last name (ascending), then by last name (ascending), then by suffix (ascending). (DONE)

# Service (COMPLETED)
 
- Create a CongregationPersonService that
  - Accepts a congregationId, a person and a position (String).  Create a person, and create the relationship between the person and the congregation. (DONE)
  - Updates a person/position combination (DONE)

- Create a PersonService that
  - Retrieves all persons (delegated to repository) (DONE)
  - Creates new persons (DONE)
  - Updates a person (DONE)
  - Deletes a person (after deleting all congregation relationships) (DONE)

# Controller (COMPLETED)
 
- Create a PersonController with endpoints that handle all person CRUD concerns by delegating them to services. (DONE)
- Create a CongregationPersonController with endpoints that handle all congregation-person CRUD concerns by delegating them to services. (DONE)

# Implementation Plan

## 1. DTOs (COMPLETED)
- **PersonDto.java**: Complete person fields including `List<CongregationRelationDto> congregations`. (DONE)
- **PersonRelationDto.java**: Represents a person within a congregation (id, firstName, lastName, position). (DONE)
- **CongregationRelationDto.java**: Represents a congregation for a person (id, name, position). (DONE)
- Update **CongregationDto.java**: Include `List<PersonRelationDto> persons`. (DONE)

## 2. Repository (COMPLETED)
- Update **PersonRepository.java**: Add a method using `@Query(value = "SELECT * FROM person ORDER BY last_name ASC, first_name ASC, suffix ASC", nativeQuery = true)` to support the requested ordering. (DONE)

## 3. Services (COMPLETED)
- **PersonService.java**: Implement full CRUD for `PersonEntity`. Deletion will rely on JPA cascading to clean up relationships. (DONE)
- **CongregationPersonService.java**: Implement relationship management. (DONE)
  - `addPersonToCongregation`: Creates both the `PersonEntity` (if new) and the `CongregationPersonEntity` relationship. (DONE)
  - `updatePersonPosition`: Updates the `position` field in an existing relationship. (DONE)

## 4. Mappers (COMPLETED)
- **PersonMapper.java**: Manual mapping component for converting between entities and DTOs. (DONE)
- Update **CongregationMapper.java**: Incorporate `PersonRelationDto` into the existing `CongregationDto` mapping. (DONE)

## 5. Controllers (COMPLETED)
- **PersonController.java**: REST endpoints for person CRUD operations. (DONE)
- **CongregationPersonController.java**: REST endpoints for managing the link between persons and congregations. (DONE)

## 6. Testing (COMPLETED)
- Create unit and integration tests to verify all new functionality and mappings. (DONE)
