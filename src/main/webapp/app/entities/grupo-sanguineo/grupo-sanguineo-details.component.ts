import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IGrupoSanguineo } from '@/shared/model/grupo-sanguineo.model';

import GrupoSanguineoService from './grupo-sanguineo.service';

export default defineComponent({
  name: 'GrupoSanguineoDetails',
  setup() {
    const grupoSanguineoService = inject('grupoSanguineoService', () => new GrupoSanguineoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const grupoSanguineo: Ref<IGrupoSanguineo> = ref({});

    const retrieveGrupoSanguineo = async grupoSanguineoId => {
      try {
        const res = await grupoSanguineoService().find(grupoSanguineoId);
        grupoSanguineo.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.grupoSanguineoId) {
      retrieveGrupoSanguineo(route.params.grupoSanguineoId);
    }

    return {
      alertService,
      grupoSanguineo,

      previousState,
    };
  },
});
