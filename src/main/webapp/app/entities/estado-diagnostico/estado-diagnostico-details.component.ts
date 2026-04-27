import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IEstadoDiagnostico } from '@/shared/model/estado-diagnostico.model';

import EstadoDiagnosticoService from './estado-diagnostico.service';

export default defineComponent({
  name: 'EstadoDiagnosticoDetails',
  setup() {
    const estadoDiagnosticoService = inject('estadoDiagnosticoService', () => new EstadoDiagnosticoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const estadoDiagnostico: Ref<IEstadoDiagnostico> = ref({});

    const retrieveEstadoDiagnostico = async estadoDiagnosticoId => {
      try {
        const res = await estadoDiagnosticoService().find(estadoDiagnosticoId);
        estadoDiagnostico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.estadoDiagnosticoId) {
      retrieveEstadoDiagnostico(route.params.estadoDiagnosticoId);
    }

    return {
      alertService,
      estadoDiagnostico,

      previousState,
    };
  },
});
