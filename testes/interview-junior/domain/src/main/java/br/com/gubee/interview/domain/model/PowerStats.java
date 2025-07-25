package br.com.gubee.interview.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PUBLIC;

@Data
@NoArgsConstructor(access = PUBLIC)
@AllArgsConstructor
@Builder
public class PowerStats {

    private UUID id;

    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;
    private Instant createdAt;
    private Instant updatedAt;


}
