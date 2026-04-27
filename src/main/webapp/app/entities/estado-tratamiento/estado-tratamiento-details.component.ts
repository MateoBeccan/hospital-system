import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IEstadoTratamiento } from '@/shared/model/estado-tratamiento.model';

import EstadoTratamientoService from './estado-tratamiento.service';

export default defineComponent({
  name: 'EstadoTratamientoDetails',
  setup() {
    const estadoTratamientoService = inject('estadoTratamientoService', () => new EstadoTratamientoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const estadoTratamiento: Ref<IEstadoTratamiento> = ref({});

    const retrieveEstadoTratamiento = async estadoTratamientoId => {
      try {
        const res = await estadoTratamientoService().find(estadoTratamientoId);
        estadoTratamiento.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.estadoTratamientoId) {
      retrieveEstadoTratamiento(route.params.estadoTratamientoId);
    }

    return {
      alertService,
      estadoTratamiento,

      previousState,
    };
  },
});
