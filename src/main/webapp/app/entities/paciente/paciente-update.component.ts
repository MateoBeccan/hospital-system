import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import FactorRhService from '@/entities/factor-rh/factor-rh.service';
import GrupoSanguineoService from '@/entities/grupo-sanguineo/grupo-sanguineo.service';
import ObraSocialService from '@/entities/obra-social/obra-social.service';
import PersonaService from '@/entities/persona/persona.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IFactorRh } from '@/shared/model/factor-rh.model';
import { type IGrupoSanguineo } from '@/shared/model/grupo-sanguineo.model';
import { type IObraSocial } from '@/shared/model/obra-social.model';
import { type IPaciente, Paciente } from '@/shared/model/paciente.model';
import { type IPersona } from '@/shared/model/persona.model';

import PacienteService from './paciente.service';

export default defineComponent({
  name: 'PacienteUpdate',
  setup() {
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paciente: Ref<IPaciente> = ref(new Paciente());

    const personaService = inject('personaService', () => new PersonaService());

    const personas: Ref<IPersona[]> = ref([]);

    const obraSocialService = inject('obraSocialService', () => new ObraSocialService());

    const obraSocials: Ref<IObraSocial[]> = ref([]);

    const grupoSanguineoService = inject('grupoSanguineoService', () => new GrupoSanguineoService());

    const grupoSanguineos: Ref<IGrupoSanguineo[]> = ref([]);

    const factorRhService = inject('factorRhService', () => new FactorRhService());

    const factorRhs: Ref<IFactorRh[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePaciente = async pacienteId => {
      try {
        const res = await pacienteService().find(pacienteId);
        paciente.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pacienteId) {
      retrievePaciente(route.params.pacienteId);
    }

    const initRelationships = () => {
      personaService()
        .retrieve()
        .then(res => {
          personas.value = res.data;
        });
      obraSocialService()
        .retrieve()
        .then(res => {
          obraSocials.value = res.data;
        });
      grupoSanguineoService()
        .retrieve()
        .then(res => {
          grupoSanguineos.value = res.data;
        });
      factorRhService()
        .retrieve()
        .then(res => {
          factorRhs.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      numeroHistoriaClinica: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 3 caracteres.', 3),
        maxLength: validations.maxLength('Este campo no puede superar más de 40 caracteres.', 40),
      },
      alergiasGenerales: {},
      observaciones: {},
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      persona: {
        required: validations.required('Este campo es obligatorio.'),
      },
      obraSocial: {},
      grupoSanguineo: {},
      factorRh: {},
      historiaClinica: {},
    };
    const v$ = useVuelidate(validationRules, paciente as any);
    v$.value.$validate();

    return {
      pacienteService,
      alertService,
      paciente,
      previousState,
      isSaving,
      currentLanguage,
      personas,
      obraSocials,
      grupoSanguineos,
      factorRhs,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.paciente.id) {
        this.pacienteService()
          .update(this.paciente)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Paciente is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.pacienteService()
          .create(this.paciente)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Paciente is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
