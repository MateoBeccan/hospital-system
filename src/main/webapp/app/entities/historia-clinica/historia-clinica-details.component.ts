import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IHistoriaClinica } from '@/shared/model/historia-clinica.model';

import HistoriaClinicaService from './historia-clinica.service';

export default defineComponent({
  name: 'HistoriaClinicaDetails',
  setup() {
    const historiaClinicaService = inject('historiaClinicaService', () => new HistoriaClinicaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const historiaClinica: Ref<IHistoriaClinica> = ref({});

    const retrieveHistoriaClinica = async historiaClinicaId => {
      try {
        const res = await historiaClinicaService().find(historiaClinicaId);
        historiaClinica.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.historiaClinicaId) {
      retrieveHistoriaClinica(route.params.historiaClinicaId);
    }

    return {
      alertService,
      historiaClinica,

      ...dataUtils,

      previousState,
    };
  },
});
