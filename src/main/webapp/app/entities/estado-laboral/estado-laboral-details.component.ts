import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IEstadoLaboral } from '@/shared/model/estado-laboral.model';

import EstadoLaboralService from './estado-laboral.service';

export default defineComponent({
  name: 'EstadoLaboralDetails',
  setup() {
    const estadoLaboralService = inject('estadoLaboralService', () => new EstadoLaboralService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const estadoLaboral: Ref<IEstadoLaboral> = ref({});

    const retrieveEstadoLaboral = async estadoLaboralId => {
      try {
        const res = await estadoLaboralService().find(estadoLaboralId);
        estadoLaboral.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.estadoLaboralId) {
      retrieveEstadoLaboral(route.params.estadoLaboralId);
    }

    return {
      alertService,
      estadoLaboral,

      previousState,
    };
  },
});
