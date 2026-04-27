import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import ConsultaService from '@/entities/consulta/consulta.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IConsulta } from '@/shared/model/consulta.model';
import { type ISignosVitales, SignosVitales } from '@/shared/model/signos-vitales.model';

import SignosVitalesService from './signos-vitales.service';

export default defineComponent({
  name: 'SignosVitalesUpdate',
  setup() {
    const signosVitalesService = inject('signosVitalesService', () => new SignosVitalesService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const signosVitales: Ref<ISignosVitales> = ref(new SignosVitales());

    const consultaService = inject('consultaService', () => new ConsultaService());

    const consultas: Ref<IConsulta[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSignosVitales = async signosVitalesId => {
      try {
        const res = await signosVitalesService().find(signosVitalesId);
        res.fechaHoraRegistro = new Date(res.fechaHoraRegistro);
        signosVitales.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.signosVitalesId) {
      retrieveSignosVitales(route.params.signosVitalesId);
    }

    const initRelationships = () => {
      consultaService()
        .retrieve()
        .then(res => {
          consultas.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      fechaHoraRegistro: {
        required: validations.required('Este campo es obligatorio.'),
      },
      peso: {
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      talla: {
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      temperatura: {
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      presionArterial: {
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      frecuenciaCardiaca: {
        integer: validations.integer('Este campo debe ser un número.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      frecuenciaRespiratoria: {
        integer: validations.integer('Este campo debe ser un número.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      saturacionOxigeno: {
        integer: validations.integer('Este campo debe ser un número.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
        max: validations.maxValue('Este campo no puede ser mayor que 100.', 100),
      },
      observaciones: {},
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      consulta: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, signosVitales as any);
    v$.value.$validate();

    return {
      signosVitalesService,
      alertService,
      signosVitales,
      previousState,
      isSaving,
      currentLanguage,
      consultas,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: signosVitales }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.signosVitales.id) {
        this.signosVitalesService()
          .update(this.signosVitales)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A SignosVitales is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.signosVitalesService()
          .create(this.signosVitales)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A SignosVitales is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
