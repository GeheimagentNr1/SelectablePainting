package de.geheimagentnr1.selectable_painting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SelectablePaintingTest {

    @Test
    void modIdIsValid() {

        String modId = "selectable_painting";
        assertTrue( modId.matches( "[a-z][a-z0-9_]{1,63}" ) );
    }
}
