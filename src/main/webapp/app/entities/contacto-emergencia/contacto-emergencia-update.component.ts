import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PersonaService from '@/entities/persona/persona.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { ContactoEmergencia, type IContactoEmergencia } from '@/shared/model/contacto-emergencia.model';
import { type IPersona } from '@/shared/model/persona.model';

import ContactoEmergenciaService from './contacto-emergencia.service';

export default defineComponent({
  name: 'ContactoEmergenciaUpdate',
  setup() {
    const contactoEmergenciaService = inject('contactoEmergenciaService', () => new ContactoEmergenciaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const contactoEmergencia: Ref<IContactoEmergencia> = ref(new ContactoEmergencia());

    const personaService = inject('personaService', () => new PersonaService());

    const personas: Ref<IPersona[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveContactoEmergencia = async contactoEmergenciaId => {
      try {
        const res = await contactoEmergenciaService().find(contactoEmergenciaId);
        contactoEmergencia.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.contactoEmergenciaId) {
      retrieveContactoEmergencia(route.params.contactoEmergenciaId);
    }

    const initRelationships = () => {
      personaService()
        .retrieve()
        .then(res => {
          personas.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 2 caracteres.', 2),
        maxLength: validations.maxLength('Este campo no puede superar más de 150 caracteres.', 150),
      },
      telefono: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      parentesco: {
        maxLength: validations.maxLength('Este campo no puede superar más de 80 caracteres.', 80),
      },
      observaciones: {
        maxLength: validations.maxLength('Este campo no puede superar más de 255 caracteres.', 255),
      },
      prioridad: {
        required: validations.required('Este campo es obligatorio.'),
        integer: validations.integer('Este campo debe ser un número.'),
        min: validations.minValue('Este campo debe ser mayor que 1.', 1),
        max: validations.maxValue('Este campo no puede ser mayor que 10.', 10),
      },
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      persona: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, contactoEmergencia as any);
    v$.value.$validate();

    return {
      contactoEmergenciaService,
      alertService,
      contactoEmergencia,
      previousState,
      isSaving,
      currentLanguage,
      personas,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.contactoEmergencia.id) {
        this.contactoEmergenciaService()
          .update(this.contactoEmergencia)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A ContactoEmergencia is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.contactoEmergenciaService()
          .create(this.contactoEmergencia)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A ContactoEmergencia is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
