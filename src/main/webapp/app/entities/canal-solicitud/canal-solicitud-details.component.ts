import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ICanalSolicitud } from '@/shared/model/canal-solicitud.model';

import CanalSolicitudService from './canal-solicitud.service';

export default defineComponent({
  name: 'CanalSolicitudDetails',
  setup() {
    const canalSolicitudService = inject('canalSolicitudService', () => new CanalSolicitudService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const canalSolicitud: Ref<ICanalSolicitud> = ref({});

    const retrieveCanalSolicitud = async canalSolicitudId => {
      try {
        const res = await canalSolicitudService().find(canalSolicitudId);
        canalSolicitud.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.canalSolicitudId) {
      retrieveCanalSolicitud(route.params.canalSolicitudId);
    }

    return {
      alertService,
      canalSolicitud,

      previousState,
    };
  },
});
