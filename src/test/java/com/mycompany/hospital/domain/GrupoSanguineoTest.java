package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.GrupoSanguineoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GrupoSanguineoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GrupoSanguineo.class);
        GrupoSanguineo grupoSanguineo1 = getGrupoSanguineoSample1();
        GrupoSanguineo grupoSanguineo2 = new GrupoSanguineo();
        assertThat(grupoSanguineo1).isNotEqualTo(grupoSanguineo2);

        grupoSanguineo2.setId(grupoSanguineo1.getId());
        assertThat(grupoSanguineo1).isEqualTo(grupoSanguineo2);

        grupoSanguineo2 = getGrupoSanguineoSample2();
        assertThat(grupoSanguineo1).isNotEqualTo(grupoSanguineo2);
    }
}
