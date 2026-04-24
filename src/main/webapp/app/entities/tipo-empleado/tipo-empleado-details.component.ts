import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ITipoEmpleado } from '@/shared/model/tipo-empleado.model';

import TipoEmpleadoService from './tipo-empleado.service';

export default defineComponent({
  name: 'TipoEmpleadoDetails',
  setup() {
    const tipoEmpleadoService = inject('tipoEmpleadoService', () => new TipoEmpleadoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const tipoEmpleado: Ref<ITipoEmpleado> = ref({});

    const retrieveTipoEmpleado = async tipoEmpleadoId => {
      try {
        const res = await tipoEmpleadoService().find(tipoEmpleadoId);
        tipoEmpleado.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tipoEmpleadoId) {
      retrieveTipoEmpleado(route.params.tipoEmpleadoId);
    }

    return {
      alertService,
      tipoEmpleado,

      previousState,
    };
  },
});
