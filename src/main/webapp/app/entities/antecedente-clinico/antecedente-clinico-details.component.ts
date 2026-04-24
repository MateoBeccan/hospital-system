import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IAntecedenteClinico } from '@/shared/model/antecedente-clinico.model';

import AntecedenteClinicoService from './antecedente-clinico.service';

export default defineComponent({
  name: 'AntecedenteClinicoDetails',
  setup() {
    const antecedenteClinicoService = inject('antecedenteClinicoService', () => new AntecedenteClinicoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const antecedenteClinico: Ref<IAntecedenteClinico> = ref({});

    const retrieveAntecedenteClinico = async antecedenteClinicoId => {
      try {
        const res = await antecedenteClinicoService().find(antecedenteClinicoId);
        antecedenteClinico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.antecedenteClinicoId) {
      retrieveAntecedenteClinico(route.params.antecedenteClinicoId);
    }

    return {
      alertService,
      antecedenteClinico,

      ...dataUtils,

      previousState,
    };
  },
});
