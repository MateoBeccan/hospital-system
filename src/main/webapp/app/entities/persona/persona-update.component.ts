import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import CiudadService from '@/entities/ciudad/ciudad.service';
import SexoService from '@/entities/sexo/sexo.service';
import TipoDocumentoService from '@/entities/tipo-documento/tipo-documento.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type ICiudad } from '@/shared/model/ciudad.model';
import { type IPersona, Persona } from '@/shared/model/persona.model';
import { type ISexo } from '@/shared/model/sexo.model';
import { type ITipoDocumento } from '@/shared/model/tipo-documento.model';

import PersonaService from './persona.service';

export default defineComponent({
  name: 'PersonaUpdate',
  setup() {
    const personaService = inject('personaService', () => new PersonaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const persona: Ref<IPersona> = ref(new Persona());

    const tipoDocumentoService = inject('tipoDocumentoService', () => new TipoDocumentoService());

    const tipoDocumentos: Ref<ITipoDocumento[]> = ref([]);

    const sexoService = inject('sexoService', () => new SexoService());

    const sexos: Ref<ISexo[]> = ref([]);

    const ciudadService = inject('ciudadService', () => new CiudadService());

    const ciudads: Ref<ICiudad[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePersona = async personaId => {
      try {
        const res = await personaService().find(personaId);
        persona.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.personaId) {
      retrievePersona(route.params.personaId);
    }

    const initRelationships = () => {
      tipoDocumentoService()
        .retrieve()
        .then(res => {
          tipoDocumentos.value = res.data;
        });
      sexoService()
        .retrieve()
        .then(res => {
          sexos.value = res.data;
        });
      ciudadService()
        .retrieve()
        .then(res => {
          ciudads.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 2 caracteres.', 2),
        maxLength: validations.maxLength('Este campo no puede superar más de 100 caracteres.', 100),
      },
      apellido: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 2 caracteres.', 2),
        maxLength: validations.maxLength('Este campo no puede superar más de 100 caracteres.', 100),
      },
      nroDocumento: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 5 caracteres.', 5),
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      fechaNacimiento: {},
      telefono: {
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      email: {
        maxLength: validations.maxLength('Este campo no puede superar más de 191 caracteres.', 191),
      },
      direccion: {
        maxLength: validations.maxLength('Este campo no puede superar más de 255 caracteres.', 255),
      },
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      tipoDocumento: {
        required: validations.required('Este campo es obligatorio.'),
      },
      sexo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      ciudad: {},
      paciente: {},
      empleado: {},
    };
    const v$ = useVuelidate(validationRules, persona as any);
    v$.value.$validate();

    return {
      personaService,
      alertService,
      persona,
      previousState,
      isSaving,
      currentLanguage,
      tipoDocumentos,
      sexos,
      ciudads,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.persona.id) {
        this.personaService()
          .update(this.persona)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Persona is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.personaService()
          .create(this.persona)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Persona is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
