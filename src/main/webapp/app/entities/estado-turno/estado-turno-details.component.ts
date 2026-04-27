import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IEstadoTurno } from '@/shared/model/estado-turno.model';

import EstadoTurnoService from './estado-turno.service';

export default defineComponent({
  name: 'EstadoTurnoDetails',
  setup() {
    const estadoTurnoService = inject('estadoTurnoService', () => new EstadoTurnoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const estadoTurno: Ref<IEstadoTurno> = ref({});

    const retrieveEstadoTurno = async estadoTurnoId => {
      try {
        const res = await estadoTurnoService().find(estadoTurnoId);
        estadoTurno.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.estadoTurnoId) {
      retrieveEstadoTurno(route.params.estadoTurnoId);
    }

    return {
      alertService,
      estadoTurno,

      previousState,
    };
  },
});
